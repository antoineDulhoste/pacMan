package application;

import java.util.HashMap;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Editeur extends Stage{
	
	private final int size = 16;
	private final double MULTI = 1.5;
	
	GridPane grid = new GridPane();
	VBox buttons = new VBox();
	
	HBox root = new HBox(grid, new Separator(Orientation.VERTICAL), buttons);
	
	public int pen = -1;
	private int[][] map;
	
	private HashMap<Integer, Integer> teleporteur = new HashMap<>();
	private HashMap<Integer, Color> teleporteurColor = new HashMap<>();
	
	int index = 0;
	
	private String name;
	private int pacStartX = -1;
	private int pacStartY = -1;
	private int pinkyStartX = -1;
	private int pinkyStartY = -1;
	private int blinkyStartX = -1;
	private int blinkyStartY = -1;
	
	public Editeur(Level lvl) {
		this.name = lvl.getName();
		Main.menu.close();
		Scene scene = new Scene(root);
		
		map = lvl.map;
		grid.setGridLinesVisible(true);
		for(int l = 0; l<map.length; l++) {
			for (int c = 0; c<map[l].length; c++) {	
				Rectangle rect = new Rectangle(c*size*MULTI, l*size*MULTI, size*MULTI, size*MULTI);
				rect.setOnMouseClicked(e->{
					int ll = (int) (rect.getY()/size/MULTI);
					int lc = (int) (rect.getX()/size/MULTI);
					int value = map[ll][lc];
					if(value > 50 && pen != value && teleporteur.containsKey(value)) {
						teleporteur.put(value, teleporteur.get(value)-1);
					}
					if(pen > -1) {
						if(pen == 0) {
							rect.setFill(Color.YELLOW);
							map[ll][lc] = pen;
						} else if(pen == 1) {
							rect.setFill(Color.BLUE);
							map[ll][lc] = pen;
						} else if(pen == 2) {
							rect.setFill(Color.BLACK);
							map[ll][lc] = pen;
						} else if(pen == 8) {
							rect.setFill(Color.BLACK);
							map[ll][lc] = pen;
						} else if(pen == 10) {
							//PacMan start
							pacStartX = ll;
							pacStartY = lc;
							rect.setFill(Color.BURLYWOOD);
						} else if(pen == 11) {
							//PacMan start
							pinkyStartX = ll;
							pinkyStartY = lc;
							rect.setFill(Color.PINK);
						} else if(pen == 12) {
							//PacMan start
							blinkyStartX = ll;
							blinkyStartY = lc;
							rect.setFill(Color.RED);
						} else if(pen > 50) {
							if(teleporteur.containsKey(value)) {
								if(teleporteur.get(value) <= 2) {
									map[ll][lc] = pen;
									rect.setFill(teleporteurColor.get(value));
									teleporteur.put(value, teleporteur.get(value)+1);
								} 
							}else {
								map[ll][lc] = pen;
								teleporteur.put(value, 1);
								teleporteurColor.put(value, randomColor());
								rect.setFill(teleporteurColor.get(value));
							}
						}
					}
					
				});	
				int value = map[l][c];
				if(value == 1 ) {
					rect.setFill(Color.BLUE);				
				}else if(value == 2 ) {
					rect.setFill(Color.YELLOW);
				}else if(value == 8 ) {
					rect.setFill(Color.BLACK);
				}else if(value == 0 ) {
					rect.setFill(Color.BLACK);
				}else if(value > 50 ) {
					if(teleporteur.containsKey(value)) {
						rect.setFill(teleporteurColor.get(value));
						teleporteur.put(value, teleporteur.get(value)+1);
					}else {
						teleporteur.put(value, 1);
						teleporteurColor.put(value, randomColor());
						rect.setFill(teleporteurColor.get(map[l][c]));
					}
				}
				grid.add(rect, c, l);
			}
		}
		
		/**/
		buttons.getChildren().add(new ClickableText("Chemin avec gum", 0, this));
		buttons.getChildren().add(new ClickableText("Chemin sans gum", 2, this));
		buttons.getChildren().add(new ClickableText("Case hors map", 8, this));
		buttons.getChildren().add(new ClickableText("Mur", 1, this));
		buttons.getChildren().add(new ClickableText("Pacman depart", 10, this));
		buttons.getChildren().add(new ClickableText("Pinky depart", 11, this));
		buttons.getChildren().add(new ClickableText("Blinky depart", 12, this));
		Spinner<Integer> spin = new Spinner<>(51, 99, 51);
		spin.valueProperty().addListener(l->{
			ClickableText.texts.forEach(e->{
				e.setFill(Color.BLACK);
			});
			pen = spin.getValue();
		});
		CustomText save = new CustomText("Sauvegarder");
		save.setOnMouseClicked(e->{
			Level creation = new Level(name, map, pacStartX, pacStartY, blinkyStartX, blinkyStartY, pinkyStartX, pinkyStartY);
			if(Level.levels.contains(creation)) {
				Level.levels.remove(creation);
			}
			Level.levels.add(creation);
			exit();
		});
		buttons.getChildren().add(spin);
		buttons.getChildren().add(save);
		CustomText quit = new CustomText("Quitter");
		quit.setFill(Color.RED);
		quit.setOnMouseClicked(e->{
			Main.menu = new Menu();
			close();
		});
		buttons.getChildren().add(quit);
		this.setScene(scene);
		this.show();
	}
	
	public Editeur(int width, int height, String name) {
		this.name = name;
		Main.menu.close();
		Scene scene = new Scene(root);
		
		map = new int[height][width];
		grid.setGridLinesVisible(true);
		for(int l = 0; l<height; l++) {
			for (int c = 0; c<width; c++) {	
				map[l][c] = 2;
				Rectangle rect = new Rectangle(c*size*MULTI, l*size*MULTI, size*MULTI, size*MULTI);
				rect.setOnMouseClicked(e->{
					int ll = (int) (rect.getY()/size/MULTI);
					int lc = (int) (rect.getX()/size/MULTI);
					int value = map[ll][lc];
					if(value > 50 && pen != value && teleporteur.containsKey(value)) {
						teleporteur.put(value, teleporteur.get(value)-1);
					}
					if(pen > -1) {
						if(pen == 0) {
							rect.setFill(Color.YELLOW);
							map[ll][lc] = pen;
						} else if(pen == 1) {
							rect.setFill(Color.BLUE);
							map[ll][lc] = pen;
						} else if(pen == 2) {
							rect.setFill(Color.BLACK);
							map[ll][lc] = pen;
						} else if(pen == 8) {
							rect.setFill(Color.BLACK);
							map[ll][lc] = pen;
						} else if(pen == 10) {
							//PacMan start
							pacStartX = ll;
							pacStartY = lc;
							rect.setFill(Color.BURLYWOOD);
						} else if(pen == 11) {
							//PacMan start
							pinkyStartX = ll;
							pinkyStartY = lc;
							rect.setFill(Color.PINK);
						} else if(pen == 12) {
							//PacMan start
							blinkyStartX = ll;
							blinkyStartY = lc;
							rect.setFill(Color.RED);
						} else if(pen > 50) {
							if(teleporteur.containsKey(value)) {
								if(teleporteur.get(value) <= 2) {
									map[ll][lc] = pen;
									rect.setFill(teleporteurColor.get(value));
									teleporteur.put(value, teleporteur.get(value)+1);
								} 
							}else {
								map[ll][lc] = pen;
								teleporteur.put(value, 1);
								teleporteurColor.put(value, randomColor());
								rect.setFill(teleporteurColor.get(value));
							}
						}
					}
					
				});		
				rect.setFill(Color.BLACK);
				grid.add(rect, c, l);
			}
		}
		
		/**/
		buttons.getChildren().add(new ClickableText("Chemin avec gum", 0, this));
		buttons.getChildren().add(new ClickableText("Chemin sans gum", 2, this));
		buttons.getChildren().add(new ClickableText("Case hors map", 8, this));
		buttons.getChildren().add(new ClickableText("Mur", 1, this));
		buttons.getChildren().add(new ClickableText("Pacman depart", 10, this));
		buttons.getChildren().add(new ClickableText("Pinky depart", 11, this));
		buttons.getChildren().add(new ClickableText("Blinky depart", 12, this));
		Spinner<Integer> spin = new Spinner<>(51, 99, 51);
		spin.valueProperty().addListener(l->{
			ClickableText.texts.forEach(e->{
				e.setFill(Color.BLACK);
			});
			pen = spin.getValue();
		});
		CustomText save = new CustomText("Sauvegarder");
		save.setOnMouseClicked(e->{
			Level creation = new Level(name, map, pacStartX, pacStartY, blinkyStartX, blinkyStartY, pinkyStartX, pinkyStartY);
			if(Level.levels.contains(creation)) {
				Level.levels.remove(creation);
			}
			Level.levels.add(creation);
			exit();
		});
		buttons.getChildren().add(spin);
		buttons.getChildren().add(save);
		CustomText quit = new CustomText("Quitter");
		quit.setFill(Color.RED);
		quit.setOnMouseClicked(e->{
			Main.menu = new Menu();
			close();
		});
		buttons.getChildren().add(quit);
		this.setScene(scene);
		this.show();
	}
	
	public void exit() {
		Main.menu = new Menu();
		this.close();
		Level.saveLevels();
		Level.loadLevels();
	}

	private Color randomColor() {
		return new Color(Math.random(), Math.random(), Math.random(), 1);
	}
}
