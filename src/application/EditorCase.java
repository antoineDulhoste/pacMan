package application;

import javafx.scene.shape.Rectangle;

public class EditorCase extends Rectangle{
	
	private int value;
	
	public EditorCase(double x, double y, double w, double h, int value) {
		super(x, y, w, h);
		this.value = value;
	}
	public EditorCase(double x, double y, int value) {
		super(x, y);
		this.value = value;
	}
	public EditorCase(int value) {
		this.value = value;
	}


	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
