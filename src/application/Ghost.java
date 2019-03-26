package application;

public abstract class Ghost extends Entite implements Runnable {

	private int[][] plateau;
	
	public Ghost(double x, double y, int[][] plateau) {
		super(x, y);
		this.plateau = plateau;
	}
	
	@Override
	public abstract void run();
	
	public boolean canMoveVertically(Double addY) {
		if(addY > 0) {
			Double y = this.y + 0.495;
			y+=addY;
			if(plateau[this.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = this.x - 0.495;
			if(plateau[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = this.x + 0.495;
			if(plateau[topRight.intValue()][y.intValue()] == 1) return false;
		}else {
			Double y = this.y - 0.495;
			y+=addY;
			if(plateau[this.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = this.x - 0.495;
			if(plateau[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = this.x + 0.495;
			if(plateau[topRight.intValue()][y.intValue()] == 1) return false;
		}
		return true;
	}
	
	public boolean canMoveHorizontally(Double addX) {
		boolean sortie = true;
		if(addX > 0) {
			Double x = this.x + 0.495;
			x+=addX;
			if(plateau[x.intValue()][this.y.intValue()] == 1) sortie = false;
			Double topLeft = this.y - 0.495;
			if(plateau[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = this.y + 0.495;
			if(plateau[x.intValue()][topRight.intValue()] == 1) sortie = false;
			
		}else {
			Double x = (Double)this.x - 0.495;
			x+=addX;
			if(plateau[x.intValue()][this.y.intValue()] == 1) sortie = false;
			Double topLeft = this.y - 0.495;
			if(plateau[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = this.y + 0.495;
			if(plateau[x.intValue()][topRight.intValue()] == 1) sortie = false;
		}
		return sortie;
	}

}
