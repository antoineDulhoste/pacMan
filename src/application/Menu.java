package application;


import java.io.File;
import java.util.Optional;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Menu extends Stage {
	
	private Scene scene;
	private Group root = new Group();
	private int compteur=0;
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}

	public Group getRoot() {
		return root;
	}

	private Timeline timeline;
	
	public Menu() {
		this.start();
	}
	
	public void t1() {
		Text t1 = new Text();
        t1.setText("press ENTER \n"
        		+ "  to START");
        t1.setEffect(null);
        t1.setOpacity(1);
        //time qui permet de faire clignoter le press to start
        timeline = new Timeline(new KeyFrame(
        		Duration.millis(500), 
        		event-> {
        			if (t1.getOpacity()==1) {
        				t1.setOpacity(0);
        				
        			} else {
        				t1.setOpacity(1);
        			}
        		}
		));
        timeline.setCycleCount( Animation.INDEFINITE);
        timeline.play();
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(150);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        root.getChildren().add(t1);
	}
	
	private void start() {
		scene = new Scene(root, 600, 750, Color.BLACK);
		String imageURI = new File("icone.png").toURI().toString(); 
        Image image = new Image(imageURI);
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
        t1();
        //ajout de la detection de la touche entree
        scene.addEventFilter(KeyEvent.KEY_PRESSED,new ActionEntree(this));
        scene.addEventFilter(KeyEvent.KEY_PRESSED,new ActionEchap(this));
        this.getIcons().add(image);
		this.setTitle("PacMan");
		this.setScene(scene);
		this.show();
	}
	
	public void menuMulti() {
		Text t1 = new Text();
        t1.setText("server");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(220);
        t1.setY(550);
        t1.setFill(Color.WHITE);
        t1.setCursor(Cursor.CLOSED_HAND);
        t1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.out.println("Start PacMan MULTI: SERVER");
            	Net.startServer(7778);
            	
            }
        });
        root.getChildren().add(t1);
        
        Text t2 = new Text();
        t2.setText("connect");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(210);
        t2.setY(620);
        t2.setFill(Color.WHITE);
        t2.setCursor(Cursor.CLOSED_HAND);
        t2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	compteur=3;
            	menuRemove(2);
            	actionConnect();
            }
        });
        root.getChildren().add(t2);
	}
	
	 //ajoute les boutons solo,multi,quitter
	public void menu1() {
    	Text t1 = new Text();
        t1.setText("solo");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(250);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        t1.setCursor(Cursor.CLOSED_HAND);
        t1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	close();
            	try {
            		Level level;
            		ChoiceDialog<Level> dialog = new ChoiceDialog<>(Level.levels.get(0), Level.levels);
					dialog.setTitle("Choisir un niveau");
					dialog.setHeaderText("Look, a Choice Dialog");
					dialog.setContentText("Choose your letter:");

					// Traditional way to get the response value.
					Optional<Level> result = dialog.showAndWait();
					if (result.isPresent()){
						level = result.get();
					    System.out.println("Level ("+level.getName()+") OK");
					    Jeu jeu = new Jeu(Muliplayer.SOLO, level);
					}
        		} catch(Exception e) {
        			e.printStackTrace();
        		}
            	
            	
            }
        });
        root.getChildren().add(t1);
        
        Text t2 = new Text();
        t2.setText("multi");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(240);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        t2.setCursor(Cursor.CLOSED_HAND);
        t2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	setCompteur(2);
            	menuRemove(3);
            	menuMulti();
            }
        });
        root.getChildren().add(t2);
        
        Text t3 = new Text();
        t3.setText("exit");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(250);
        t3.setY(640);
        t3.setCursor(Cursor.CLOSED_HAND);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.exit(0);
            }
        });
        root.getChildren().add(t3);
    }
	
	public void menuConfigServer() {
		Text t1 = new Text();
        t1.setText("PORT:");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(130);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        root.getChildren().add(t1);
        
        TextField port = new TextField ();
        port.setLayoutX(285);
        port.setLayoutY(460);
        port.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(port);
        
        Text t2 = new Text();
        t2.setText("CARTE:");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(130);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        root.getChildren().add(t2);
        
        ComboBox<Level> levels = new ComboBox<>(FXCollections.observableArrayList(Level.levels));
        levels.setLayoutX(285);
        levels.setLayoutY(530);      
        levels.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(levels);
        
        Text t3 = new Text();
        t3.setText("VALIDER");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(130);
        t3.setY(550);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	try {
            		System.out.println("PACMAN SERVER:\nPORT: "+Integer.parseInt(t1.getText())+"\nLEVEL: "+levels.getValue().getName());
                	Net.startServer(Integer.parseInt(t1.getText()));
            	}catch(Exception ex) {
            		
            	}
            }
        });
        root.getChildren().add(t3);
	}

	public void actionConnect() {
		Text t1 = new Text();
        t1.setText("IP:");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(130);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        root.getChildren().add(t1);
        
        TextField ip = new TextField ();
        ip.setLayoutX(285);
        ip.setLayoutY(460);
        ip.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(ip);
        
        Text t2 = new Text();
        t2.setText("PORT:");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(130);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        root.getChildren().add(t2);
        
        TextField port = new TextField ();
        port.setLayoutX(285);
        port.setLayoutY(530);
        port.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(port);
        
        Text t3 = new Text();
        t3.setText("valider");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(240);
        t3.setY(650);
        t3.setFill(Color.WHITE);
        t3.setCursor(Cursor.CLOSED_HAND);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.out.println("Start PacMan MULTI: CLIENT");
            	Net.joinServer(ip.getText(), 7778);
            }
        });
        root.getChildren().add(t3);
	}
	
	public void menuRemove(int c) {
		for(int i=0;i<c;i++) {
			root.getChildren().remove(1);
		}
	}
}
