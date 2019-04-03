package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import PathFinding.Node;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
 
import static java.util.stream.Collectors.*;

import java.awt.TextField;

import static java.util.Map.Entry.*;

public class ViewJeu extends Stage{
	
	private static final double MULTI = 2.0;
	private Jeu jeu;
	/* Layout */
	private Group root = new Group();
	private Scene scene;
	/* Nodes */
	Circle player;
	Circle blinky;
	Circle pinky;
	
	/* Animations des murs */
	ArrayList<Rectangle> walls = new ArrayList<>(); 
	int wallCursor = 0;
	int wallColor = 0;
	boolean gameover = false;
	Text scoreJeu = new Text("0000");
	
	/* Affichage du path <"x;y", Rectangle>*/
	HashMap<String, Rectangle> paths = new HashMap<>();
	
	/* Threads */
	AnimationTimer ATCoins;
	MediaPlayer mediaplayer;
	Thread TMusique;
	Timeline timelineDeplacements;
	Timeline wallAnimation;
	Timeline timelineGameOver;
	
	/** Final Timelines **/
	Timeline TLPathFinding;
		
	public ViewJeu(Jeu jeu, Muliplayer muliplayer) {
		Main.menu.close();
		this.jeu = jeu;
		try {
			if(muliplayer == Muliplayer.SERVER) {
				this.setTitle("PacMan SERVER");
				//startServer();
			} else if (muliplayer == Muliplayer.CLIENT) {
				this.setTitle("PacMan CLIENT");
				//joinServer();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		start();
	}
	
	private void start() {
		/* Creation de la scene a la taille de la map avec le fond noir */
		//if(scene == null)
		scene = new Scene(root, jeu.level.map[0].length*jeu.size*MULTI, jeu.level.map.length*jeu.size*MULTI, Color.BLACK);
		
		/** On construit le plateau de jeu **/
		for(int l = 0; l<jeu.level.map.length; l++) {
			for (int c = 0; c<jeu.level.map[l].length; c++) {				
				Rectangle rect = new Rectangle(c*jeu.size*MULTI, l*jeu.size*MULTI, jeu.size*MULTI, jeu.size*MULTI);
				if(jeu.level.map[l][c] == 1 ) {
					rect.setFill(Color.BLUE);
					walls.add(rect);
				}else if(jeu.level.map[l][c] == 2 ) {
					rect.setFill(Color.BLACK);
				}else {
					rect.setFill(Color.BLACK);
					paths.put(l+";"+c, rect);
				}
				root.getChildren().add(rect);
			}
		}
		
		/*Dessine le joueur*/
		player = new Circle(jeu.player.y*jeu.player.size*MULTI, jeu.player.x*jeu.player.size*MULTI, jeu.player.rayon*MULTI);
		player.setFill(Color.YELLOW);
		root.getChildren().add(player);
		
		blinky = new Circle(jeu.blinky.y*jeu.blinky.size*MULTI, jeu.blinky.x*jeu.blinky.size*MULTI, jeu.blinky.rayon*MULTI);
        blinky.setFill(Color.RED);
        root.getChildren().add(blinky); 
        
        pinky = new Circle(jeu.pinky.y*jeu.pinky.size*MULTI, jeu.pinky.x*jeu.pinky.size*MULTI, jeu.pinky.rayon*MULTI);
        pinky.setFill(Color.DEEPPINK);
        root.getChildren().add(pinky); 
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	switch (event.getCode()) {
                    case UP:    jeu.player.dirWanted = 4; break;
                    case DOWN:  jeu.player.dirWanted = 2; break;
                    case LEFT:  jeu.player.dirWanted = 1; break;
                    case RIGHT: jeu.player.dirWanted = 3; break; 
                    case ENTER: if(!gameover) {
			                    	GameOver(); 
			                    	if(jeu.multiplayer == Muliplayer.SERVER) {
			            				Net.sendData("3/GHOST");
			            			} else if(jeu.multiplayer == Muliplayer.CLIENT) {
			            				Net.sendData("3/PACMAN");
			            			}
			                    } else validationScore();
                    			
                    			break;
            	}
            }
        });
        
        scene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            if(deltaY < 0) {
            	mediaplayer.setVolume(mediaplayer.getVolume()-0.05);
            } else {
            	mediaplayer.setVolume(mediaplayer.getVolume()+0.05);
            } 
        });
        
        /* Thread */
        ATCoins = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
            	/* On regarde les collisions de PacMan avec les Gums */
                for(Gum c : jeu.gums) {
                	Shape intersect = Shape.intersect(player, c);
                    if(intersect.getBoundsInLocal().getWidth() != -1){
                    	jeu.removeGum(c);
                    	break;
                    }
                }
                /* Si il n'y a plus de gums en jeu victoire de PacMan*/
                if(jeu.gums.isEmpty()) {
                	/* Si jeu en multi il faut avertir l'autre joueur */
                	if(jeu.isServer()) Net.sendData("3/PACMAN");
                	/* On execute l'animation de GameOver et on fini la partie */
                	GameOver();
                }
            }
        };
        /* Si le joueur joue Pinky il ne doit pas rammaser les gums */
        if(!jeu.isClient()) ATCoins.start();
        
        /* Thread concernant la musique */
        TMusique = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
		    		String uriString = new File("song.mp3").toURI().toString();
		    		mediaplayer = new MediaPlayer(new Media(uriString));
		    		mediaplayer.setAutoPlay(true);
		    		mediaplayer.setVolume(0.01);
		    		mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
		    	    }catch(Exception ex){ 
		    		   ex.printStackTrace();
		    	   }
			}
		});
        TMusique.start();
        
        	timelineDeplacements = new Timeline(
            	    new KeyFrame(
            	        Duration.millis( 2 ),
            	        event -> {
            	        	Double newX = 0.0;
        	            	Double newY = 0.0;
        	            	if (jeu.player.dirWanted == 4) {
        	            		newX -= 0.01;
        	                } else if (jeu.player.dirWanted == 2) {
        	                	newX += 0.01;
        	                } else if (jeu.player.dirWanted == 1) {
        	                	newY -= 0.01;
        	                } else if (jeu.player.dirWanted == 3) {
        	                	newY += 0.01;
        	                } else return;
        	            	boolean moved = false;
        	            	if(!newY.equals(0.0)) {
        	            		if(jeu.canMoveVertically(newY)) {
        	            			jeu.movePlayerY(newY);
        	            			moved = true;
        	            		}
        	            	}    
        	            	if(!newX.equals(0.0)) {
        	            		if(jeu.canMoveHorizontally(newX)) {
        	            			jeu.movePlayerX(newX);
        	            			moved = true;
        	            		}
        	            	}
        	            	if(moved) {
        	            		jeu.player.dir = jeu.player.dirWanted;
        	            	} else {
        	            		newX = 0.0;
        		            	newY = 0.0;
        		            	if (jeu.player.dir == 4) {
        		            		newX -= 0.01;
        		                } else if (jeu.player.dir == 2) {
        		                	newX += 0.01;
        		                } else if (jeu.player.dir == 1) {
        		                	newY -= 0.01;
        		                } else if (jeu.player.dir == 3) {
        		                	newY += 0.01;
        		                } else return;
        		            	moved = false;
        		            	if(!newY.equals(0.0)) {
        		            		if(jeu.canMoveVertically(newY)) {
        		            			jeu.movePlayerY(newY);
        		            			moved = true;
        		            		}
        		            	}    
        		            	if(!newX.equals(0.0)) {
        		            		if(jeu.canMoveHorizontally(newX)) {
        		            			jeu.movePlayerX(newX);
        		            			moved = true;
        		            		}
        		            	}
        	            	}
            	        }
            	    )
            	);
			scoreJeu.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22));
			scoreJeu.setX(22);
			scoreJeu.setY(22);
			root.getChildren().add(scoreJeu);
			scoreJeu.setFill(Color.WHITE);
    		timelineDeplacements.setCycleCount( Animation.INDEFINITE );
       
    		/* Si le joueur joue Pinky on interverti les deux entites */
    		if(jeu.isClient()) {
    			double x = jeu.pinky.x;
    			double y = jeu.pinky.y;
    			
    			jeu.pinky.x = jeu.player.x;
    			jeu.pinky.y = jeu.player.y;
    			
    			jeu.player.x = x;
    			jeu.player.y = y;
    			
    			pinky.setFill(Color.YELLOW);
    			player.setFill(Color.YELLOW);
    			renderPlayer();
    			renderGhost();
    		}
        
		timelineDeplacements.play();
        
		wallAnimation = new Timeline(
	        	    new KeyFrame(
	        	        Duration.millis( 5 ),
	        	        event -> {
	        	        	walls.get(wallCursor).setFill(Tools.rainbow[wallColor]);
	        	        	wallColor ++;
	        	        	wallCursor ++;
	        	        	if(wallColor >= Tools.rainbow.length) wallColor = 0;
	        	        	if(wallCursor >= walls.size()) wallCursor = 0;
	        	        }
	        	    )
	        	);
		wallAnimation.setCycleCount( Animation.INDEFINITE );
		//wallAnimation.play();
		
		timelineGameOver = new Timeline(
	        	    new KeyFrame(
	        	        Duration.millis( 20 ),
	        	        event -> {
	        	        	Shape intersectBlinky = Shape.intersect(player, blinky);
	                        if(intersectBlinky.getBoundsInLocal().getWidth() != -1)
	                        {
	                        	Platform.runLater(new Runnable() {		
									@Override
									public void run() {
										if(jeu.multiplayer == Muliplayer.SERVER) Net.sendData("3/BLINKY");
										GameOver();		
									}
								});
	                        }
	                        
	                        Shape intersectPinky = Shape.intersect(player, pinky);
	                        if(intersectPinky.getBoundsInLocal().getWidth() != -1)
	                        {
	                        	Platform.runLater(new Runnable() {		
									@Override
									public void run() {
										if(jeu.multiplayer == Muliplayer.SERVER) Net.sendData("3/BLINKY");
										GameOver();		
									}
								});
	                        }
	        	        }
	        	    )
	        	);
		timelineGameOver.setCycleCount( Animation.INDEFINITE );
		if(jeu.multiplayer != Muliplayer.CLIENT) timelineGameOver.play();
		
		this.setTitle("PacMan");
		this.setScene(scene);
		this.show();
		
		jeu.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				boolean skip = false;
				renderPlayer();
				//renderGhost();
				if( arg instanceof Gum ) {
					Gum gum = (Gum) arg;
					if ( root.getChildren().contains(gum)) {
						root.getChildren().remove(gum);
						scoreJeu.setText(jeu.score+"");
						skip = true;
					} else {
						root.getChildren().add(gum);
						skip = true;
					}
				}
				
				if( arg instanceof String) {
					String str = (String) arg;
					if (str.equals("RENDEROTHER")) {
						renderOtherNET();
						skip = true;
					}
					if (str.equals("BLINKY")) {
						renderBlinky();
						skip = true;
					}
					if (str.equals("PINKY")) {
						renderPinky();
						skip = true;
					}
					if (str.equals("GAMEOVER")) {
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								GameOver();
							}
						});
						
						skip = true;
					}
				}
				if(skip) return;
				try {
					if(jeu.isServer()) {
						Net.sendData("4/"+jeu.ServerSendData());
					}else if (jeu.isClient()) {
						Net.sendData("4/"+jeu.ClientSendData());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		
		/* Pour equilibrer si le joueur joue Pinky il ne voit pas les gums restants */
		if(!jeu.isClient()) addGums();
		
		if(jeu.isServer() || jeu.isSolo()) {
			timelinePath.setCycleCount(Animation.INDEFINITE);
			timelinePath.play();
		}
		
		String imageURI = new File("icone.png").toURI().toString(); 
		Image image = new Image(imageURI);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
			private boolean b=true;
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ESCAPE && b==true){
					b=false;
					close();
					new Menu();
				}
			}
		});
	}
	
	private void addGums() {
		for(int l = 0; l<jeu.level.map.length; l++) {
			for (int c = 0; c<jeu.level.map[l].length; c++) {
				if(jeu.level.map[l][c] == 0) {
					Gum gum = new Gum(c*16*MULTI+(16/2*MULTI), l*16*MULTI+(16/2*MULTI), 2*MULTI, 1);
					jeu.addGum(gum);
				}
			}
		}
	}
	
	private void startMusique() {
		mediaplayer.play();
	}
	
	private void stopMusique() {
		mediaplayer.stop();
	}
	
	private void startAllThread() {
		ATCoins.start();
		timelineDeplacements.play();
		timelineGameOver.play();
	}
	
	private void stopAllThread() {
		ATCoins.stop();
		timelineGameOver.stop();
		timelineDeplacements.stop();
		timelinePath.stop();
	}
	javafx.scene.control.TextField tf = new javafx.scene.control.TextField();
	private void GameOver() {
			gameover = true;
		 stopAllThread();
		 for(Rectangle r : paths.values()) root.getChildren().remove(r);
		 for(Gum c : jeu.gums) root.getChildren().remove(c);
		 root.getChildren().remove(scoreJeu);
		 final Timeline timeline = new Timeline(
	        	    new KeyFrame(
	        	        Duration.millis( 20 ),
	        	        event -> {
	        	        	for(Rectangle r : walls) {
	        	    			r.setY(r.getY()+10);
	        	    			if(r.getY() > jeu.level.map.length*16*MULTI + 16) {
	        	    				root.getChildren().remove(r);	        	    				
	        	    			}
	        	    		}
	        	        	if (jeu.player.dir == 4) {
	                    		jeu.player.x -= 0.1;
	                        } else if (jeu.player.dir == 2) {
	                        	jeu.player.x += 0.1;
	                        } else if (jeu.player.dir == 1) {
	                        	jeu.player.y -= 0.1;
	                        } else if (jeu.player.dir == 3) {
	                        	jeu.player.y += 0.1;
	                        } else jeu.player.y += 0.1;
	        	        	renderPlayer();
	        	        	
	        	        	if (jeu.pinky.dir == 4) {
	                    		jeu.pinky.x -= 0.1;
	                        } else if (jeu.pinky.dir == 2) {
	                        	jeu.pinky.x += 0.1;
	                        } else if (jeu.pinky.dir == 1) {
	                        	jeu.pinky.y -= 0.1;
	                        } else if (jeu.pinky.dir == 3) {
	                        	jeu.pinky.y += 0.1;
	                        } else jeu.pinky.y += 0.1;
	        	        	
	        	        	if (jeu.blinky.dir == 4) {
	                    		jeu.blinky.x -= 0.1;
	                        } else if (jeu.blinky.dir == 2) {
	                        	jeu.blinky.x += 0.1;
	                        } else if (jeu.blinky.dir == 1) {
	                        	jeu.blinky.y -= 0.1;
	                        } else if (jeu.blinky.dir == 3) {
	                        	jeu.blinky.y += 0.1;
	                        } else jeu.blinky.y += 0.1;
	        	        	renderGhost();
	        	        }
	        	    )
	        	);
	        	timeline.setCycleCount( Animation.INDEFINITE );
	        	timeline.play();
	        	
	        	
	        	/* Ajoute un texte */
	        	loadScores();
	        	scores.put("<SYSNON>", jeu.score);
	        	System.out.println(scores.size());
	        	HashMap<String, Integer> sorted = scores
	        	        .entrySet()
	        	        .stream()
	        	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	        	        .collect(
	        	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	        	                LinkedHashMap::new));
	        	Text victoire;
	        	System.err.println(jeu.gums.size());
	        	if(jeu.gums.isEmpty()) {
	        		victoire = new Text("Victoire de Pacman");
	        	}else {
	        		victoire = new Text("Victoire des Fantomes");
	        	}
	        	victoire.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        	victoire.setX(22*MULTI);
	        	victoire.setY(50*MULTI);
	        	victoire.setFill(Color.WHITE);
	        	Text scorePacMan = new Text("Score : "+jeu.score);
	        	scorePacMan.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        	scorePacMan.setX(22*MULTI);
	        	scorePacMan.setY(100*MULTI);
	        	scorePacMan.setFill(Color.WHITE);
	        	tf.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20;");
	        	int number = 1;
	        	boolean deja = false;
	        	for(String s : sorted.keySet()) {
	        		if(number < 5) {
	        			if(s.equals("<SYSNON>") && !deja) {
	        				deja = true;
	        				Text id = new Text(number+" ");
	        				id.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        				id.setX(22*MULTI);
	        				id.setY((100+number*50)*MULTI);
	        				id.setFill(Color.WHITE);
	        				
	        				tf.setTranslateX(60*MULTI);
	        				tf.setTranslateY((100+number*50-20)*MULTI);
	        				Text tscore = new Text(jeu.score+"");
	        				tscore.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        				tscore.setX(250*MULTI);
	        				tscore.setY((100+number*50)*MULTI);
	        				tscore.setFill(Color.WHITE);
	        	        	root.getChildren().addAll(id, tf, tscore);
	        			}else {
	        				Text id = new Text(number+" "+s+" : ");
	        				id.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        				id.setX(22*MULTI);
	        				id.setY((100+number*50)*MULTI);
	        				id.setFill(Color.WHITE);
	        	        	
	        	        	Text tscore = new Text(sorted.get(s)+"");
	        	        	tscore.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
	        	        	tscore.setX(250*MULTI);
	        	        	tscore.setY((100+number*50)*MULTI);
	        	        	tscore.setFill(Color.WHITE);
	        	        	root.getChildren().addAll(id, tscore);
	        			}
	        			number++;
	        		} else break;
	        	}
	        	root.getChildren().addAll(victoire, scorePacMan);
	        	final Timeline timelinerestart = new Timeline(
	        	 	    new KeyFrame(
	        	 	        Duration.seconds(5),
	        	 	        event -> {
	        	 	        	//this.jeu = new Jeu();
	        	 	        	root.getChildren().clear();
	        	 	        	start();
	        	 	        	timeline.stop();
	        	 	        })
	        	 	    );
	        	//timelinerestart.play();
	        	jeu.gums.clear();
	}
	private void renderPlayer() {
		//Mise a jour de la position du joueur
		player.setCenterX(jeu.player.y*jeu.player.size*MULTI);
		player.setCenterY(jeu.player.x*jeu.player.size*MULTI);
	}
	
	private void renderPlayerNET() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				root.getChildren().remove(player);
				player = new Circle(jeu.player.y*jeu.player.size*MULTI, jeu.player.x*jeu.player.size*MULTI, jeu.player.rayon*MULTI);
				player.setFill(Color.YELLOW);
				root.getChildren().add(player);
			}
		});
	}
	
	private void renderOtherNET() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				root.getChildren().remove(pinky);
				pinky = new Circle(jeu.pinky.y*jeu.pinky.size*MULTI, jeu.pinky.x*jeu.pinky.size*MULTI, jeu.pinky.rayon*MULTI);
				pinky.setFill(Color.DEEPPINK);
				root.getChildren().add(pinky);

				root.getChildren().remove(blinky);
				blinky = new Circle(jeu.blinky.y*jeu.blinky.size*MULTI, jeu.blinky.x*jeu.blinky.size*MULTI, jeu.blinky.rayon*MULTI);
				blinky.setFill(Color.RED);
				root.getChildren().add(blinky);
			}
		});
	}
	
	private void renderBlinky() {
		blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
		blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
		if(jeu.multiplayer == Muliplayer.SERVER) {
			Net.sendData("5/"+jeu.blinky.x+";"+jeu.blinky.y);
		}
	}
	
	private void renderPinky() {
		pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
		pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
	}
	private void renderGhost() {
		//Mise a jour de la position de blinky
		blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
		blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
		
		pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
		pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
	}
	
	private boolean showingPath = true;
	final Timeline timelinePath = new Timeline(
	 	    new KeyFrame(
	 	        Duration.millis( 250 ),
	 	        event -> {
	 	        	if(showingPath && jeu.multiplayer != Muliplayer.CLIENT) {
	 	        		//System.out.println("Path"); 
	 	        		//paths.values().forEach(r->{
	 	        		//	r.setFill(Color.BLACK);
	 	        		//});
	 	        		List<Node> nodesBlinky = jeu.pathMap.findPath(jeu.blinky.x.intValue(), jeu.blinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
	 	        		//nodesBlinky.forEach(n->{
	 	        		//	paths.get(n.getX()+";"+n.getY()).setFill(Color.INDIANRED);
	 	        		//});
	 	        		jeu.moveBlinky(nodesBlinky.get(0).getX()+0.5, nodesBlinky.get(0).getY()+0.5);
	 	        		List<Node> nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
	 	        		try {
        	        		switch (jeu.player.dir) {
            	        	case 1: 
            	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue() - 3);
            	        		break;
            	        	case 2: 
            	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue() + 3, jeu.player.y.intValue());
            	        		break;
            	        	case 3: 
            	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue() + 3);
            	        		break;
            	        	case 4: 
            	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue() - 3, jeu.player.y.intValue());
            	        		break;
            	        	default:
            	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
            	        		break;
            	        	}
        	        	}catch(Exception ex) {
        	        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
        	        	}
	 	        		if(nodesPinky == null || nodesPinky.isEmpty()) {
	 	        			nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
	 	        		}
	 	        		//nodesPinky.forEach(n->{
	 	        		//	paths.get(n.getX()+";"+n.getY()).setFill(Color.PINK);
	 	        		//});
	 	        		//paths.get(nodesPinky.get(0).getX()+";"+nodesPinky.get(0).getY()).setFill(Color.PINK);
	 	        		if(jeu.multiplayer == Muliplayer.SOLO) {
	 	        			if(nodesPinky != null && !nodesPinky.isEmpty())
	 	        				jeu.movePinky(nodesPinky.get(0).getX()+0.5, nodesPinky.get(0).getY()+0.5);
	 	        		}
	 	        	}	
	        }
	    )
	);

	
	/* SERVER */
	private ServerSocket serverSocket;
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public void sendData(String str) throws Exception{
		dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(str);
	}

	private void startServer() throws Exception {
		System.out.println("Starting Server .b.");
		serverSocket = new ServerSocket(7777);
		System.out.println("Server Started\nWaiting for connection ...");
		socket = serverSocket.accept();
		System.out.println("Connection from: "+ socket.getInetAddress());
		
		dis = new DataInputStream(socket.getInputStream());	
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					while((dis = new DataInputStream(socket.getInputStream())) != null) {
						String str = dis.readUTF();
						System.out.println("S Receive: "+str);
						ServerReceiveData(str);
					}
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	private void joinServer() throws Exception{
		System.out.println("Connection to server");
		socket = new Socket("localhost", 7777);
		dis = new DataInputStream(socket.getInputStream());

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					while((dis = new DataInputStream(socket.getInputStream())) != null) {
						String str = dis.readUTF();
						System.out.println("C Receive: "+str);
						ClientReceiveData(str);
					}
				}catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		}).start();
	}
	
	public void ClientReceiveData(String str) {
		String[] list = str.split(";");
		jeu.pinky.x = Double.parseDouble(list[0]);
		jeu.pinky.y = Double.parseDouble(list[1]);
		renderOtherNET();
	}
	
	public void ServerReceiveData(String str) {
		String[] list = str.split(";");
		jeu.pinky.x = Double.parseDouble(list[0]);
		jeu.pinky.y = Double.parseDouble(list[1]);
		renderOtherNET();
	}
	
	private void setFirstPlan() {
		jeu.gums.forEach(g->{
			root.getChildren().remove(g);
			root.getChildren().add(g);
		});
	}
	static HashMap<String, Integer> scores = new HashMap<String, Integer>();
	private void loadScores() {
		try
        {
            FileInputStream fis = new FileInputStream("Scores");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            scoresperMap = (HashMap<String, HashMap<String, Integer>>) ois.readObject();
            if(scoresperMap.containsKey(jeu.level.getName())) {
            	scores = scoresperMap.get(jeu.level.getName());
            }else {
            	scores = new HashMap<>();
            }
            scores.forEach((k, v)->System.out.println(k+""+v));
            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();  
        }
	}
	HashMap<String, HashMap<String, Integer>> scoresperMap = new HashMap<>();
	private void saveScore() {
		try
        {
            FileOutputStream fos = new FileOutputStream("Scores");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scoresperMap);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
	}
	
	private void validationScore() {
		scores.remove("<SYSNON>");
		if(!tf.getText().isEmpty()) {
			scores.put(tf.getText(), jeu.score);
			
		}
		scoresperMap.put(jeu.level.getName(), scores);
		saveScore();
		this.close();
		Main.menu = new Menu();
	}
}
