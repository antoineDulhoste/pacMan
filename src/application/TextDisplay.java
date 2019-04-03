package application;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TextDisplay extends Text {

	
	public TextDisplay(double textSize, double X, double Y, String text) {
		super(text);
		this.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,textSize));
		this.setX(X);
		this.setY(Y);
		this.setFill(Color.WHITE);
	}
}
