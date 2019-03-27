package application;


import java.io.File;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Menu extends Stage {
	

	public Timeline getTimeline() {
		return timeline;
	}

	private Scene scene;
	private Group root = new Group();
	
	public Group getRoot() {
		return root;
	}

	private Timeline timeline;
	
	public Menu() {
		this.start();
	}
	
	private void start() {
		scene = new Scene(root, 600, 750, Color.BLACK);
		String imageURI = new File("icone.png").toURI().toString(); 
        Image image = new Image(imageURI);
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
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
        //ajout de la detection de la touche entree
        scene.addEventFilter(KeyEvent.KEY_PRESSED,new ActionEntree(this));
        this.getIcons().add(image);
		this.setTitle("PacMan");
		this.setScene(scene);
		this.show();
	}
}
