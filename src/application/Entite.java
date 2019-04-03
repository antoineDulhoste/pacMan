package application;

import java.util.TimerTask;

public abstract class Entite{
	
	public Double x, y;
	public int dir = 0;
	public int dirWanted = 0;
	public int size = 16;
	public int rayon = 8;
	
	public Entite(double startX, double startY) {
		this.x = startX;
		this.y = startY;
	}
	
	public void setPos(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

}
