package application;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static Menu menu;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			menu = new Menu();
			new Editeur(20, 20, "Yolo 2");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Level.loadLevels();
		launch(args);
	}
	
	
}
