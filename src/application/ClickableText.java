package application;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ClickableText extends Text{
	
	public static ArrayList<ClickableText> texts = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private int value;
	@SuppressWarnings("unused")
	private Editeur edit;
	
	public ClickableText(String text, int value, Editeur edit) {
		super(text);
		this.value = value;
		this.edit = edit;
		texts.add(this);
		this.setOnMouseClicked(e->{
			texts.forEach(e2->{
				e2.setFill(Color.BLACK);
			});
			edit.pen = value;
			setFill(Color.BLUE);
		});
		this.setFont(Font.font ("Comic Sans MS",FontWeight.BOLD,20));
	}

}
