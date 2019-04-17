package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import static java.util.stream.Collectors.*;

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
	
	ArrayList<Rectangle> paths = new ArrayList<>();
	
	/* Threads */
	AnimationTimer ATCoins;
	static MediaPlayer mediaplayer;
	static Thread TMusique = new Thread(new Runnable() {
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
	});;
	Timeline timelineSpriteAnimation;
	int PacSprite = 1;
	Timeline timelineDeplacements;
	Timeline wallAnimation;
	Timeline timelineGameOver;
	
	/** Final Timelines **/
	Timeline TLPathFinding;
		
	/* Sprites */
	Image PMR1 = new Image("Sprites/PMR1.jpg");
	Image PMR2 = new Image("Sprites/PMR2.jpg");
	Image PMR3 = new Image("Sprites/PMR3.jpg");
	Image PMR4 = new Image("Sprites/PMR4.jpg");
	Image PMR5 = new Image("Sprites/PMR5.jpg");
	Image PMR6 = new Image("Sprites/PMR6.jpg");
	
	Image PML1 = new Image("Sprites/PML1.jpg");
	Image PML2 = new Image("Sprites/PML2.jpg");
	Image PML3 = new Image("Sprites/PML3.jpg");
	Image PML4 = new Image("Sprites/PML4.jpg");
	Image PML5 = new Image("Sprites/PML5.jpg");
	Image PML6 = new Image("Sprites/PML6.jpg");
	
	Image PMU1 = new Image("Sprites/PMU1.jpg");
	Image PMU2 = new Image("Sprites/PMU2.jpg");
	Image PMU3 = new Image("Sprites/PMU3.jpg");
	Image PMU4 = new Image("Sprites/PMU4.jpg");
	Image PMU5 = new Image("Sprites/PMU5.jpg");
	Image PMU6 = new Image("Sprites/PMU6.jpg");
	
	Image PMD1 = new Image("Sprites/PMD1.jpg");
	Image PMD2 = new Image("Sprites/PMD2.jpg");
	Image PMD3 = new Image("Sprites/PMD3.jpg");
	Image PMD4 = new Image("Sprites/PMD4.jpg");
	Image PMD5 = new Image("Sprites/PMD5.jpg");
	Image PMD6 = new Image("Sprites/PMD6.jpg");
	
	Image PMGO1 = new Image("Sprites/PMGO1.jpg");
	Image PMGO2 = new Image("Sprites/PMGO2.jpg");
	Image PMGO3 = new Image("Sprites/PMGO3.jpg");
	Image PMGO4 = new Image("Sprites/PMGO4.jpg");
	Image PMGO5 = new Image("Sprites/PMGO5.jpg");
	Image PMGO6 = new Image("Sprites/PMGO6.jpg");
	
	Image PinkyDown1 = new Image("Sprites/PinkyDown1.jpg");
	Image PinkyDown2 = new Image("Sprites/PinkyDown2.jpg");
	Image PinkyDown3 = new Image("Sprites/PinkyDown3.jpg");
	Image PinkyDown4 = new Image("Sprites/PinkyDown4.jpg");
	Image PinkyDown5 = new Image("Sprites/PinkyDown5.jpg");
	Image PinkyDown6 = new Image("Sprites/PinkyDown6.jpg");
	
	Image PinkyUp1 = new Image("Sprites/PinkyUp1.jpg");
	Image PinkyUp2 = new Image("Sprites/PinkyUp2.jpg");
	Image PinkyUp3 = new Image("Sprites/PinkyUp3.jpg");
	Image PinkyUp4 = new Image("Sprites/PinkyUp4.jpg");
	Image PinkyUp5 = new Image("Sprites/PinkyUp5.jpg");
	Image PinkyUp6 = new Image("Sprites/PinkyUp6.jpg");
	
	Image PinkyLeft1 = new Image("Sprites/PinkyLeft1.jpg");
	Image PinkyLeft2 = new Image("Sprites/PinkyLeft2.jpg");
	Image PinkyLeft3 = new Image("Sprites/PinkyLeft3.jpg");
	Image PinkyLeft4 = new Image("Sprites/PinkyLeft4.jpg");
	Image PinkyLeft5 = new Image("Sprites/PinkyLeft5.jpg");
	Image PinkyLeft6 = new Image("Sprites/PinkyLeft6.jpg");
	
	Image PinkyRight1 = new Image("Sprites/PinkyRight1.jpg");
	Image PinkyRight2 = new Image("Sprites/PinkyRight2.jpg");
	Image PinkyRight3 = new Image("Sprites/PinkyRight3.jpg");
	Image PinkyRight4 = new Image("Sprites/PinkyRight4.jpg");
	Image PinkyRight5 = new Image("Sprites/PinkyRight5.jpg");
	Image PinkyRight6 = new Image("Sprites/PinkyRight6.jpg");
	
	Image BlinkyDown1 = new Image("Sprites/BlinkyDown1.jpg");
	Image BlinkyDown2 = new Image("Sprites/BlinkyDown2.jpg");
	Image BlinkyDown3 = new Image("Sprites/BlinkyDown3.jpg");
	Image BlinkyDown4 = new Image("Sprites/BlinkyDown4.jpg");
	Image BlinkyDown5 = new Image("Sprites/BlinkyDown5.jpg");
	Image BlinkyDown6 = new Image("Sprites/BlinkyDown6.jpg");
	
	Image BlinkyUp1 = new Image("Sprites/BlinkyUp1.jpg");
	Image BlinkyUp2 = new Image("Sprites/BlinkyUp2.jpg");
	Image BlinkyUp3 = new Image("Sprites/BlinkyUp3.jpg");
	Image BlinkyUp4 = new Image("Sprites/BlinkyUp4.jpg");
	Image BlinkyUp5 = new Image("Sprites/BlinkyUp5.jpg");
	Image BlinkyUp6 = new Image("Sprites/BlinkyUp6.jpg");
	
	Image BlinkyLeft1 = new Image("Sprites/BlinkyLeft1.jpg");
	Image BlinkyLeft2 = new Image("Sprites/BlinkyLeft2.jpg");
	Image BlinkyLeft3 = new Image("Sprites/BlinkyLeft3.jpg");
	Image BlinkyLeft4 = new Image("Sprites/BlinkyLeft4.jpg");
	Image BlinkyLeft5 = new Image("Sprites/BlinkyLeft5.jpg");
	Image BlinkyLeft6 = new Image("Sprites/BlinkyLeft6.jpg");
	
	Image BlinkyRight1 = new Image("Sprites/BlinkyRight1.jpg");
	Image BlinkyRight2 = new Image("Sprites/BlinkyRight2.jpg");
	Image BlinkyRight3 = new Image("Sprites/BlinkyRight3.jpg");
	Image BlinkyRight4 = new Image("Sprites/BlinkyRight4.jpg");
	Image BlinkyRight5 = new Image("Sprites/BlinkyRight5.jpg");
	Image BlinkyRight6 = new Image("Sprites/BlinkyRight6.jpg");
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
					paths.add(rect);
				}
				root.getChildren().add(rect);
			}
		}
		
		/*Dessine le joueur*/
		player = new Circle(jeu.player.y*jeu.player.size*MULTI, jeu.player.x*jeu.player.size*MULTI, jeu.player.rayon*MULTI);
		/* On initialise les sprites des elements */
		root.getChildren().add(player);
		if(jeu.isServer() || jeu.isSolo()) {
        	player.setFill(new ImagePattern(PMR1));
        }else {
        	player.setFill(new ImagePattern(PinkyUp1));
        }
		
        blinky = new Circle(jeu.blinky.y*jeu.blinky.size*MULTI, jeu.blinky.x*jeu.blinky.size*MULTI, jeu.blinky.rayon*MULTI);
        blinky.setFill(new ImagePattern(BlinkyDown1));       
        root.getChildren().add(blinky); 
        
        pinky = new Circle(jeu.pinky.y*jeu.pinky.size*MULTI, jeu.pinky.x*jeu.pinky.size*MULTI, jeu.pinky.rayon*MULTI);
        if(jeu.isServer() || jeu.isSolo()) {
        	pinky.setFill(new ImagePattern(PinkyUp1));
        }else {
        	pinky.setFill(new ImagePattern(PMR1));
        }
        root.getChildren().add(pinky); 
        
        /*
         * Timeline pour les animations 
         */
        timelineSpriteAnimation = new Timeline(
        	    new KeyFrame(
        	        Duration.millis( 100 ),
        	        event -> {
        	        	if(PacSprite == 1) {   
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD1));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU1));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML1));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR1));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown1));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp1));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft1));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight1));
	        	            	}
        	        		}else {
        	        			if(jeu.player.dir == 2) {
            	                	player.setFill(new ImagePattern(PMD1));
            	            	} else if(jeu.player.dir == 4) {
            	            		player.setFill(new ImagePattern(PMU1));
            	            	} else if(jeu.player.dir == 1) {
            	            		player.setFill(new ImagePattern(PML1));
            	            	}else if(jeu.player.dir == 3) {
            	            		player.setFill(new ImagePattern(PMR1));
            	            	}
        	        			if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown1));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp1));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft1));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight1));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown1));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp1));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft1));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight1));
        	            	}
        	                PacSprite = 2;
        	        	}else if(PacSprite == 2) {
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD2));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU2));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML2));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR2));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown2));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp2));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft2));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight2));
	        	            	}
        	        		}else {
	        	        		if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PMD2));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PMU2));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PML2));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PMR2));
	        	            	}
	        	        		if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown2));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp2));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft2));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight2));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown2));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp2));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft2));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight2));
        	            	}
        	                PacSprite = 3;
        	        	}else if(PacSprite == 3) {
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD3));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU3));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML3));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR3));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown3));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp3));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft3));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight3));
	        	            	}
        	        		}else {
	        	        		if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PMD3));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PMU3));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PML3));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PMR3));
	        	            	}
	        	        		if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown3));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp3));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft3));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight3));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown3));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp3));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft3));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight3));
        	            	}
        	                PacSprite = 4;
        	        	}else if(PacSprite == 4) {
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD4));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU4));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML4));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR4));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown4));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp4));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft4));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight4));
	        	            	}
        	        		}else {
	        	        		if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PMD4));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PMU4));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PML4));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PMR4));
	        	            	}
	        	        		if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown4));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp4));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft4));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight4));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown4));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp4));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft4));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight4));
        	            	}
        	                PacSprite = 5;
        	        	}else if(PacSprite == 5) {
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD5));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU5));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML5));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR5));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown5));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp5));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft5));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight5));
	        	            	}
        	        		}else {
	        	        		if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PMD5));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PMU5));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PML5));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PMR5));
	        	            	}
	        	        		if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown5));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp5));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft5));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight5));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown5));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp5));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft5));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight5));
        	            	}
        	                PacSprite = 6;
        	        	}else {
        	        		if(jeu.isClient()) {
        	        			if(jeu.netDir == 2) {
            	                	pinky.setFill(new ImagePattern(PMD6));
            	            	} else if(jeu.netDir == 4) {
            	            		pinky.setFill(new ImagePattern(PMU6));
            	            	} else if(jeu.netDir == 1) {
            	            		pinky.setFill(new ImagePattern(PML6));
            	            	}else if(jeu.netDir == 3) {
            	            		pinky.setFill(new ImagePattern(PMR6));
            	            	}
        	        			if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PinkyDown6));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PinkyUp6));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PinkyLeft6));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PinkyRight6));
	        	            	}
        	        		}else {
	        	        		if(jeu.player.dir == 2) {
	        	        			player.setFill(new ImagePattern(PMD6));
	        	            	} else if(jeu.player.dir == 4) {
	        	            		player.setFill(new ImagePattern(PMU6));
	        	            	} else if(jeu.player.dir == 1) {
	        	            		player.setFill(new ImagePattern(PML6));
	        	            	}else if(jeu.player.dir == 3) {
	        	            		player.setFill(new ImagePattern(PMR6));
	        	            	}
	        	        		if(jeu.isServer()) {
	        	        			if(jeu.netDir == 2) {
	            	                	pinky.setFill(new ImagePattern(PinkyDown6));
	            	            	} else if(jeu.netDir == 4) {
	            	            		pinky.setFill(new ImagePattern(PinkyUp6));
	            	            	} else if(jeu.netDir == 1) {
	            	            		pinky.setFill(new ImagePattern(PinkyLeft6));
	            	            	}else if(jeu.netDir == 3) {
	            	            		pinky.setFill(new ImagePattern(PinkyRight6));
	            	            	}
	        	        		}
        	        		}
        	        		if(jeu.blinkyDir == 2) {
        	                	blinky.setFill(new ImagePattern(BlinkyDown6));
        	            	} else if(jeu.blinkyDir == 4) {
        	            		blinky.setFill(new ImagePattern(BlinkyUp6));
        	            	} else if(jeu.blinkyDir == 1) {
        	            		blinky.setFill(new ImagePattern(BlinkyLeft6));
        	            	}else if(jeu.blinkyDir == 3) {
        	            		blinky.setFill(new ImagePattern(BlinkyRight6));
        	            	}
        	                PacSprite = 1;
        	        	}
        	        }
        	    )
        	);
        timelineSpriteAnimation.setCycleCount( Animation.INDEFINITE );
        timelineSpriteAnimation.play();

        /*
         * On ecoute les inputs du clavier
         */
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	switch (event.getCode()) {
                    case UP:    jeu.player.dirWanted = 4; break;
                    case DOWN:  jeu.player.dirWanted = 2; break;
                    case LEFT:  jeu.player.dirWanted = 1; break;
                    case RIGHT: jeu.player.dirWanted = 3; break; 
                    case ENTER: if(!gameover) {	
			                    	if(jeu.isServer()) {
			            				Net.sendData("3/GHOST");
			            			} else if(jeu.isClient()) {
			            				Net.sendData("3/PACMAN");
			            			} else if(jeu.isSolo()) jeu.victoire = TypeVictoires.GHOST;
			                    	GameOver(); 
			                    } else validationScore();		
                    			break;
					default:
						break;
            	}
            }
        });
        
        /* Thread concernant la musique */
        try {
        	TMusique.start();
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        /*
         * Utilisation de la molette pour modifier le volume
         */
        scene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            if(deltaY < 0) {
            	mediaplayer.setVolume(mediaplayer.getVolume()-0.05);
            } else {
            	mediaplayer.setVolume(mediaplayer.getVolume()+0.05);
            } 
        });
        
        /*
         * Thread qui gère les collisions avec les gums et delanche la victoire si
         * il n'y a plus de gum
         */
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
                	if(jeu.isSolo()) jeu.victoire = TypeVictoires.PACMAN;
                	/* On execute l'animation de GameOver et on fini la partie */
                	GameOver();
                }
            }
        };
        /* Si le joueur joue Pinky il ne doit pas rammaser les gums */
        if(!jeu.isClient()) ATCoins.start();
        
        
        
        /*
         * Thread qui s'occupe des déplacement
         */
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
    	timelineDeplacements.setCycleCount( Animation.INDEFINITE );
    	/*
    	 * Ajout du compteur de points
    	 */
		scoreJeu.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22));
		scoreJeu.setX(22);
		scoreJeu.setY(22);
		root.getChildren().add(scoreJeu);
		scoreJeu.setFill(Color.WHITE);
		
       
		/* Si le joueur joue Pinky on interverti les deux entites */
		if(jeu.isClient()) {
			double x = jeu.pinky.x;
			double y = jeu.pinky.y;
			
			jeu.pinky.x = jeu.player.x;
			jeu.pinky.y = jeu.player.y;
			
			jeu.player.x = x;
			jeu.player.y = y;
			
			pinky.setFill(new ImagePattern(PMU1));
			player.setFill(new ImagePattern(PinkyDown1));
			renderPlayer();
			renderGhost();
		}
        
		/*
		 * Timeline pour l'animation de la couleur des murs
		 */
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
		wallAnimation.play();
		
		/*
		 * Timeline qui verifie les collisions avec les fantomes
		 */
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
										if(jeu.isServer()) Net.sendData("3/GHOST"); jeu.victoire = TypeVictoires.GHOST;
										if(jeu.isSolo()) jeu.victoire = TypeVictoires.GHOST;
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
										if(jeu.isServer()) Net.sendData("3/GHOST"); jeu.victoire = TypeVictoires.GHOST;
										if(jeu.isSolo()) jeu.victoire = TypeVictoires.GHOST;
										GameOver();		
									}
								});
	                        }
	        	        }
	        	    )
	        	);
		timelineGameOver.setCycleCount( Animation.INDEFINITE );
		/* Les collisions sont gérées par le serveur */
		if(!jeu.isClient()) timelineGameOver.play();
		
		
		/*
		 * Observer qui met a jour suivant l'objet passé en paramètre
		 */
		jeu.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				boolean skip = false;
				renderPlayer();
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
					if (str.equals("SERVERSTART")) {			
						startGame();
						skip = true;
					}
					if (str.equals("CLIENTSTART")) {
						System.out.println("CLIENTSTART");
						NETStartGame();
						skip = true;
					}
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
		
		/*
		 * On change l'icone de la fenètre
		 */
		String imageURI = new File("icone.png").toURI().toString(); 
		Image image = new Image(imageURI);
		this.getIcons().add(image);
		
		/*
		 * Si le joueur souhaite quitter la prtie en cours
		 */
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
		
		this.setTitle("PacMan");
		this.setScene(scene);
		this.show();
		
		/*
		 * Gestion du cooldown avant le debut de la partie
		 */
		root.getChildren().add(startCoolDown);
		startCoolDown.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,50));
		startCoolDown.setFill(Color.WHITE);
		startCoolDown.setX((jeu.level.getMap().length*jeu.size*MULTI)/2-15);
		if(jeu.isClient()) {
			startCoolDown.setText("Waiting for start");
			startCoolDown.setY((jeu.level.getMap()[0].length*jeu.size*MULTI)/2-150);
		}else if(jeu.isSolo()){
			startCoolDown.setText(cooldown+"");
			startCoolDown.setY((jeu.level.getMap()[0].length*jeu.size*MULTI)/2-25);
			startGame();
		}
	}
	
	/*
	 * Methode qui ajoute les Gum sur le plateau 
	 */
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
	
	/*
	 * Arrète les threads pour quand il y a gameover
	 */
	private void stopAllThread() {
		ATCoins.stop();
		timelineGameOver.stop();
		timelineDeplacements.stop();
		timelineSpriteAnimation.stop();
	}
	
	TextField tf = new TextField();
	private void GameOver() {
		 System.out.println(jeu.victoire);
		 gameover = true;
		 jeu.switchOver();
		 stopAllThread();
		 for(Rectangle r : paths) root.getChildren().remove(r);
		 for(Gum c : jeu.gums) root.getChildren().remove(c);
		 root.getChildren().remove(scoreJeu);
		 PacSprite = 1;
		 timelineSpriteAnimation = new Timeline(
	        	    new KeyFrame(
	        	        Duration.millis( 250 ),
	        	        event -> {
	        	        	if(PacSprite == 1) {               
	        	        		player.setFill(new ImagePattern(PMGO1));
	        	                PacSprite = 2;
	        	        	}else if(PacSprite == 2) {
	        	        		player.setFill(new ImagePattern(PMGO2));
	        	                PacSprite = 3;
	        	        	}else if(PacSprite == 3) {
	        	        		player.setFill(new ImagePattern(PMGO3));
	        	                PacSprite = 4;
	        	        	}else if(PacSprite == 4) {
	        	        		player.setFill(new ImagePattern(PMGO4));
	        	                PacSprite = 5;
	        	        	}else if(PacSprite == 5) {
	        	        		player.setFill(new ImagePattern(PMGO5));
	        	                PacSprite = 6;
	        	        	}else {
	        	        		player.setFill(new ImagePattern(PMGO6));
	        	                PacSprite = 2;
	        	        	}
	        	        }
	        	    )
	        	);
        timelineSpriteAnimation.setCycleCount( Animation.INDEFINITE );
        if(jeu.victoire == TypeVictoires.GHOST) {
        	timelineSpriteAnimation.play();
        }
        /*
         * On fait sortir les entites de l'ecran
         */
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
	        	        	if(jeu.victoire == TypeVictoires.PACMAN) {
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
	        	        	}

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
        	
        	/* on charge les scores du niveau */
        	loadScores();
        	/* on ajoute une variable représentant le score actuel */
        	scores.put("<SYSNON>", jeu.score);
        	/* On trie les scores du plus haut au plus bas*/
        	HashMap<String, Integer> sorted = scores
        	        .entrySet()
        	        .stream()
        	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        	        .collect(
        	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
        	                LinkedHashMap::new));
        	Text victoire;
        	if(jeu.gums.isEmpty()) {
        		victoire = new Text("Victoire de Pacman");
        	}else {
        		victoire = new Text("Victoire des Fantomes");
        	}
        	if(jeu.isClient()) victoire.setText("Victoire de "+jeu.netWinner);
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
        	/* On place le Textfield permettant la saisie du pseudo au bon endroit sinon il n'apparait pas
        	 * il est tout de meme possible d'enregistrer un score qui n'est pas dans le top 4 en accédant u textfield via
        	 * des tabulations et de valider avec ENTER comme si on voulais valider le score
        	 */
        	for(String s : sorted.keySet()) {
        		if(number < 5) {
        			/* Si c'est le score actuel on place le texfield*/
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
        				if(!jeu.isClient()) root.getChildren().addAll(id, tf, tscore);
        			}else {
        				Text id = new Text(number+" "+s+" : ");
        				id.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,22*MULTI));
        				id.setX(22*MULTI);
        				id.setY((100+number*50)*MULTI);
        				id.setFill(Color.WHITE);
        	        	
        	        	TextDisplay tscore = new TextDisplay(22*MULTI, 250*MULTI, (100+number*50)*MULTI, sorted.get(s)+"");
        	        	if(!jeu.isClient()) root.getChildren().addAll(id, tscore);
        			}
        			number++;
        		} else break;
        	}
        	
        	if(!jeu.isClient()) {
        		if(!root.getChildren().contains(tf)) {
        			TextDisplay id = new TextDisplay(22*MULTI, 22*MULTI, (100+(number+1)*50)*MULTI, "# ");
        			tf.setTranslateX(60*MULTI);
    				tf.setTranslateY((100+(number+1)*50-20)*MULTI);
    				root.getChildren().addAll(id, tf);
        		}
        		root.getChildren().addAll(victoire, scorePacMan);
        	}
	}
	
	
	

	/** Mise a jour (graphiques) **/
	
	/*
	 * Met a jour la position du joueur
	 */
	private void renderPlayer() {
		//Mise a jour de la position du joueur
		player.setCenterX(jeu.player.y*jeu.player.size*MULTI);
		player.setCenterY(jeu.player.x*jeu.player.size*MULTI);
	}
	
	/*
	 * Met a jour la position des fantomes (Solo ou Serveur)
	 */
	private void renderGhost() {
		//Mise a jour de la position de blinky
		blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
		blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
		
		pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
		pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
	}
	
	/*
	 * Met a jour la position de pinky (Solo ou Serveur)
	 */
	private void renderPinky() {
		pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
		pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
	}
	
	/*
	 * Met a jour la position de Blinky 
	 * - Si Serveur informe le client de la position de Blinky
	 */
	private void renderBlinky() {
		blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
		blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
		if(jeu.isServer()) {
			Net.sendData("5/"+jeu.blinky.x+";"+jeu.blinky.y+";"+jeu.blinkyDir);
		}
	}
	
	/*
	 * Met a jour la position du joueur et de blinky pour le client (Online)
	 */
	private void renderOtherNET() {
		/* On execute des fonctions graphiques il faut donc un Thread spécifique*/
		Platform.runLater(new Runnable(){
			@Override
			public void run() {

				pinky.setCenterX(jeu.pinky.y*jeu.pinky.size*MULTI);
				pinky.setCenterY(jeu.pinky.x*jeu.pinky.size*MULTI);
				
				blinky.setCenterX(jeu.blinky.y*jeu.blinky.size*MULTI);
				blinky.setCenterY(jeu.blinky.x*jeu.blinky.size*MULTI);
			}
		});
	}
	
	/** Mise a jour (graphiques) (end) **/
	
	/** START GAME **/
	private Timeline timelineStartGame;
	private int cooldown = 4;
	private Text startCoolDown = new Text(cooldown+"");
	/*
	 * Démarre la partie pour si Client ou Serveur
	 */
	private void startGame() {
		timelineStartGame = new Timeline(
		 	    new KeyFrame(
	    	 	        Duration.seconds(1),
	    	 	        event -> {    	 	        	
	    	 	        	if(cooldown > 0) {
	    	 	        		cooldown--;
	    	 	        	}else {
	    	 	        		jeu.blinky.start();
	    	 	        		if(jeu.isSolo()) jeu.pinky.start();
	    	 	        		timelineDeplacements.play();
	    	 	        		root.getChildren().remove(startCoolDown);
	    	 	        		timelineStartGame.stop();
	    	 	        		if(jeu.isServer()) Net.sendData("2/Start");
	    	 	        	}
	    	 	        	startCoolDown.setText(cooldown+"");
	    	 	        })
	    	 	    );
		timelineStartGame.setCycleCount(cooldown+1);
		timelineStartGame.play();
	}
	
	/*
	 * Démarre la partie pour un client apres confirmation du serveur
	 */
	public void NETStartGame() {
 		timelineDeplacements.play();
 		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				root.getChildren().remove(startCoolDown);
			}
		});
	}
	/** START GAME (end)**/
	
	/** Gestions scores **/
	static HashMap<String, Integer> scores = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> scoresperMap = new HashMap<>();
	@SuppressWarnings("unchecked")
	private void loadScores() {
		try {
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();  
        }
	}
	
	private void saveScore() {
		try {
            FileOutputStream fos = new FileOutputStream("Scores");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scoresperMap);
            oos.close();
            fos.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
	}
	
	/*
	 * Valide le pseudo entre par le joueur puis retourne au menu
	 */
	private void validationScore() {
		if(!jeu.isClient()) {
			scores.remove("<SYSNON>");
			if(!tf.getText().isEmpty()) {
				scores.put(tf.getText(), jeu.score);	
			}
			scoresperMap.put(jeu.level.getName(), scores);
			saveScore();
		}
		this.close();
		Main.menu = new Menu();
	}
}
