package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewJeu extends Stage{
	
	private static final double MULTI = 1.0;
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
	
	/* Affichage du path <"x;y", Rectangle>*/
	HashMap<String, Rectangle> paths = new HashMap<>();
	
	/* Threads */
	AnimationTimer ATCoins;
	MediaPlayer mediaplayer;
	Thread TMusique;
	Timeline timelineDeplacements;
	Timeline wallAnimation;
	
	Muliplayer muliplayer;
	
	public ViewJeu(Jeu jeu, Muliplayer muliplayer) {
		this.jeu = jeu;
		this.muliplayer = muliplayer;
		try {
			if(muliplayer == Muliplayer.SERVER) {
				this.setTitle("PacMan SERVER");
				startServer();
			} else if (muliplayer == Muliplayer.CLIENT) {
				this.setTitle("PacMan CLIENT");
				joinServer();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		start();
	}
	
	private void start() {
		/* Creation de la scene a la taille de la map avec le fond noir */
		//if(scene == null)
		scene = new Scene(root, jeu.map[0].length*jeu.size*MULTI, jeu.map.length*jeu.size*MULTI, Color.BLACK);
		
		/** On construit le plateau de jeu **/
		for(int l = 0; l<jeu.map.length; l++) {
			for (int c = 0; c<jeu.map[l].length; c++) {				
				Rectangle rect = new Rectangle(c*jeu.size*MULTI, l*jeu.size*MULTI, jeu.size*MULTI, jeu.size*MULTI);
				if(jeu.map[l][c] == 1 ) {
					rect.setFill(Color.BLUE);
					walls.add(rect);
				}else if(jeu.map[l][c] == 2 ) {
					rect.setFill(Color.GREEN);
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
                    case ENTER: GameOver();break;
                    case SPACE: showPath(); break;
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
            	/* Collision Coins */
                for(Gum c : jeu.gums) {
                	Shape intersect = Shape.intersect(player, c);

                    if(intersect.getBoundsInLocal().getWidth() != -1)
                    {
                    	jeu.removeGum(c);
                    	break;
                    }
                    
                }
                if(jeu.gums.isEmpty()) GameOver();
            }
        };
        if(muliplayer != muliplayer.CLIENT) ATCoins.start();
        
        TMusique = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
		    		String uriString = new File("song.mp3").toURI().toString();
		    		mediaplayer = new MediaPlayer(new Media(uriString));
		    		mediaplayer.setAutoPlay(true);
		    		mediaplayer.setVolume(0.01);
		    		mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
		    	    }
		    	   catch(Exception ex)
		    	   { 
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
        	            			//jeu.player.y += newY;
        	            			jeu.movePlayerY(newY);
        	            			moved = true;
        	            		}
        	            	}    
        	            	if(!newX.equals(0.0)) {
        	            		if(jeu.canMoveHorizontally(newX)) {
        	            			//jeu.player.x += newX;
        	            			jeu.movePlayerX(newX);
        	            			moved = true;
        	            		}
        	            	}
        	            	if(moved) {
        	            		jeu.player.dir = jeu.player.dirWanted;
        	            		//renderPlayer();
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
        		            			//jeu.player.y += newY;
        		            			jeu.movePlayerY(newY);
        		            			moved = true;
        		            		}
        		            	}    
        		            	if(!newX.equals(0.0)) {
        		            		if(jeu.canMoveHorizontally(newX)) {
        		            			//jeu.player.x += newX;
        		            			jeu.movePlayerX(newX);
        		            			moved = true;
        		            		}
        		            	}
        		            	//if(moved) renderPlayer();
        		            	//renderGhost();
        	            	}
            	        }
            	    )
            	);
    		timelineDeplacements.setCycleCount( Animation.INDEFINITE );
       
    		if(muliplayer == Muliplayer.CLIENT) {
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
		
		this.setTitle("PacMan");
		this.setScene(scene);
		this.show();
		
		jeu.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				renderPlayer();
				renderGhost();
				if( arg instanceof Gum ) {
					Gum gum = (Gum) arg;
					if ( root.getChildren().contains(gum)) {
						root.getChildren().remove(gum);
					} else {
						root.getChildren().add(gum);
					}
				}
				try {
					if(muliplayer == Muliplayer.SERVER) {
						sendData(jeu.ServerSendData());
					}else if (muliplayer == Muliplayer.CLIENT) {
						System.out.println("Send to Server");
						sendData(jeu.ClientSendData());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		
		addCoins();
		//setFirstPlan();
	}
	
	private void addCoins() {
		for(int l = 0; l<jeu.map.length; l++) {
			for (int c = 0; c<jeu.map[l].length; c++) {
				if(jeu.map[l][c] == 0) {
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
	}
	
	private void stopAllThread() {
		ATCoins.stop();
		timelineDeplacements.stop();
	}
	
	private void GameOver() {
		 
		 stopAllThread();
		 for(Rectangle r : paths.values()) root.getChildren().remove(r);
		 for(Gum c : jeu.gums) root.getChildren().remove(c);
		 jeu.gums.clear();
		 final Timeline timeline = new Timeline(
	        	    new KeyFrame(
	        	        Duration.millis( 20 ),
	        	        event -> {
	        	        	for(Rectangle r : walls) {
	        	    			r.setY(r.getY()+10);
	        	    			if(r.getY() > jeu.map.length*16*MULTI + 16) {
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
	        	
	        	final Timeline timelinerestart = new Timeline(
	        	 	    new KeyFrame(
	        	 	        Duration.seconds(5),
	        	 	        event -> {
	        	 	        	this.jeu = new Jeu();
	        	 	        	root.getChildren().clear();
	        	 	        	start();
	        	 	        	timeline.stop();
	        	 	        })
	        	 	    );
	        	//timelinerestart.play();
	        	
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
			}
		});
	}
	
	private void renderGhost() {
		//Mise a jour de la position de blinky
		blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
		blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
		
		pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
		pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
	}
	
	private boolean showingPath = false;
	final Timeline timelinePath = new Timeline(
	 	    new KeyFrame(
	 	        Duration.millis( 20 ),
	 	        event -> {
	 	        	if(showingPath) {
	 	        		paths.values().forEach(r->{
	 	        			r.setFill(Color.BLACK);
	 	        		});
	 	        		List<Node> nodesBlinky = jeu.pathMap.findPath(jeu.blinky.x.intValue(), jeu.blinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
	 	        		nodesBlinky.forEach(n->{
	 	        			paths.get(n.getX()+";"+n.getY()).setFill(Color.INDIANRED);
	 	        		});
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
	 	        		
	 	        		nodesPinky.forEach(n->{
	 	        			paths.get(n.getX()+";"+n.getY()).setFill(Color.PINK);
	 	        		});
	 	        	}	
	        }
	    )
	);
     	
	
	private void showPath() {
		timelinePath.setCycleCount(Animation.INDEFINITE);
		if(showingPath) {
			showingPath = false;
			timelinePath.stop();
			paths.values().forEach(r->{
				r.setFill(Color.BLACK);
			});
			
		} else {
			showingPath = true;		
			timelinePath.play();
		}
	}
	
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
		System.out.println("Starting Server ...");
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
}
