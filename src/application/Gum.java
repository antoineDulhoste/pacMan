package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Gum extends Circle {
	
	private int value = 1;
	
	public Gum(double x, double y, double r, int value) {
		super(x, y, r);
		this.value = value;
		this.setFill(Color.YELLOW);
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
