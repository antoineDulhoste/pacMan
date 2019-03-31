package application;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
	
	public CustomText(String text) {
		super(text);
		this.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,20));
	}

}
