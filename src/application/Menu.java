package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        //timeline qui permet de faire clignoter le press to start
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
        Image image = new Image("Sprites/icone.png");
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
        t1();
        //ajout de la detection de la touche ENTER
        scene.addEventFilter(KeyEvent.KEY_PRESSED,new ActionEntree(this));
        //ajout de la detection de la touche ECHAP
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
            	setCompteur(3);
            	menuRemove(2);
            	menuConfigServer();
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
            	setCompteur(3);
            	menuRemove(2);
            	actionConnect();
            }
        });
        root.getChildren().add(t2);
	}
	
	/**
	 * Créer le menu principal avec les boutons "solo", "multi", "editor", "exit"
	 */
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
            	setCompteur(4);
            	menuRemove(4);
            	soloChooseLevel();
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
            	menuRemove(4);
            	menuMulti();
            }
        });
        root.getChildren().add(t2);
        
        Text t3 = new Text();
        t3.setText("edit map");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(190);
        t3.setY(640);
        t3.setCursor(Cursor.CLOSED_HAND);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	close();
            	new Editeur(20, 20);
            }
        });
        root.getChildren().add(t3);
        
        Text t4 = new Text();
        t4.setText("exit");
        t4.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t4.setX(250);
        t4.setY(700);
        t4.setCursor(Cursor.CLOSED_HAND);
        t4.setFill(Color.WHITE);
        t4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.exit(0);
            }
        });
        root.getChildren().add(t4);
    }
	
	/**
	 * Créer le menu pour configurer le serveur
	 */
	public void menuConfigServer() {
		Text t1 = new Text();
        t1.setText("PORT:");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(130);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        root.getChildren().add(t1);
        
        TextField port = new TextField ("7778");
        port.setTextFormatter(new TextFormatter<>(Tools.FILTER));
        port.setLayoutX(285);
        port.setLayoutY(460);
        port.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(port);
        
        Text t2 = new Text();
        t2.setText("CARTE:");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(100);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        root.getChildren().add(t2);
        
        ComboBox<Level> levels = new ComboBox<>(FXCollections.observableArrayList(Level.levels));
        levels.setLayoutX(285);
        levels.setLayoutY(530);      
        levels.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(levels);
        
        Text t3 = new Text();
        t3.setText("Valider");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(130);
        t3.setY(650);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.out.println("PACMAN SERVER:\nPORT: "+Integer.parseInt(port.getText())+"\nLEVEL: "+levels.getValue().getName());
            	setCompteur(99);
            	t3.setText("Waiting for connection");
            	t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,40));
            	new Thread(new Runnable() {
					
					@Override
					public void run() {
						Net.startServer(Integer.parseInt(port.getText()), levels.getValue());
					}
				}).start();
            	setCompteur(99);
            }
        });
        root.getChildren().add(t3);
	}

	/**
	 * Créer le menu pour se connecter à un serveur
	 */
	public void actionConnect() {
		Text t1 = new Text();
        t1.setText("IP:");
        t1.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t1.setX(130);
        t1.setY(500);
        t1.setFill(Color.WHITE);
        root.getChildren().add(t1);
        
        TextField ip = new TextField ("localhost");
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
        
        TextField port = new TextField ("7778");
        port.setTextFormatter(new TextFormatter<>(Tools.FILTER));
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
            	Net.joinServer(ip.getText(), Integer.parseInt(port.getText()));
            }
        });
        root.getChildren().add(t3);
	}
	
	/**
	 * Créer le menu pour choisir le niveau d'une partie solo et pour la lancer
	 */
	public void soloChooseLevel() {
		Text t2 = new Text();
        t2.setText("CARTE:");
        t2.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t2.setX(100);
        t2.setY(570);
        t2.setFill(Color.WHITE);
        root.getChildren().add(t2);
        
        ComboBox<Level> levels = new ComboBox<>(FXCollections.observableArrayList(Level.levels));
        levels.setLayoutX(285);
        levels.setLayoutY(530);      
        levels.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color:white; -fx-border-radius:5px; -fx-font-family:'Comic Sans MS';-fx-font-size:20");
        root.getChildren().add(levels);
        
        Text t3 = new Text();
        t3.setText("Valider");
        t3.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
        t3.setX(130);
        t3.setY(650);
        t3.setFill(Color.WHITE);
        t3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	new Jeu(Muliplayer.SOLO, levels.getValue());
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
