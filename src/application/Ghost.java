package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public abstract class Ghost extends Entite {

	private Timeline timeline = new Timeline(
	 	    new KeyFrame(
    	 	        Duration.millis(250),
    	 	        event -> {
    	 	        	run();
    	 	        })
    	 	    );
	public Jeu jeu;
	
	public Ghost(double x, double y, Jeu jeu) {
		super(x, y);
		this.jeu = jeu;
	}
	
	public void start() {
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	public void stop() {
		timeline.stop();
	};
	public abstract void run();
	
	public boolean canMoveVertically(Double addY) {
		if(addY > 0) {
			Double y = this.y + 0.495;
			y+=addY;
			if(jeu.level.map[this.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = this.x - 0.495;
			if(jeu.level.map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = this.x + 0.495;
			if(jeu.level.map[topRight.intValue()][y.intValue()] == 1) return false;
		}else {
			Double y = this.y - 0.495;
			y+=addY;
			if(jeu.level.map[this.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = this.x - 0.495;
			if(jeu.level.map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = this.x + 0.495;
			if(jeu.level.map[topRight.intValue()][y.intValue()] == 1) return false;
		}
		return true;
	}
	
	public boolean canMoveHorizontally(Double addX) {
		boolean sortie = true;
		if(addX > 0) {
			Double x = this.x + 0.495;
			x+=addX;
			if(jeu.level.map[x.intValue()][this.y.intValue()] == 1) sortie = false;
			Double topLeft = this.y - 0.495;
			if(jeu.level.map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = this.y + 0.495;
			if(jeu.level.map[x.intValue()][topRight.intValue()] == 1) sortie = false;
			
		}else {
			Double x = (Double)this.x - 0.495;
			x+=addX;
			if(jeu.level.map[x.intValue()][this.y.intValue()] == 1) sortie = false;
			Double topLeft = this.y - 0.495;
			if(jeu.level.map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = this.y + 0.495;
			if(jeu.level.map[x.intValue()][topRight.intValue()] == 1) sortie = false;
		}
		return sortie;
	}

}
