package application;

import PathFinding.PathMap;

public class Jeu {
	
	public int[][] map = {  { 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8}, 
							{ 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8}, 
							{ 8, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 8}, 
							{ 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8},							
							{ 8, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 8}, 							
							{ 8, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 8},						
							{ 8, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 8}, 							
							{ 8, 8, 8, 8, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 8, 8, 8, 8},							
							{ 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1}, 						
							{69, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,69},						
							{ 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1},							
							{ 8, 8, 8, 8, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 8, 8, 8, 8},					
							{ 8, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 8}, 						
							{ 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8}, 					
							{ 8, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 8}, 							
							{ 8, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 8},
							{ 8, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 8},
							{ 8, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 8},								
							{ 8, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 8},
							{ 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8}, 
							{ 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8}
						 };
	public final int size = 16;
	public PacMan player;
	public Blinky blinky;
	public Pinky pinky;
	
	public PathMap pathMap;
	
	public Jeu() {
		this.player = new PacMan(15.5, 10.5);
		this.blinky = new Blinky(1.5, 7.5, map);
		this.pinky = new Pinky(14.5, 2.5, map);
		this.pathMap = new PathMap(map);
	}
	
	public boolean canMoveVertically(Double addY) {
		if(addY > 0) {
			Double y = player.y + 0.495;
			y+=addY;
			if(map[player.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = player.x - 0.495;
			if(map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = player.x + 0.495;
			if(map[topRight.intValue()][y.intValue()] == 1) return false;
			if(map[player.x.intValue()][y.intValue()] >= 50) teleport(map[player.x.intValue()][y.intValue()], player.x.intValue(), y.intValue());
		}else {
			Double y = player.y - 0.495;
			y+=addY;
			if(map[player.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = player.x - 0.495;
			if(map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = player.x + 0.495;
			if(map[topRight.intValue()][y.intValue()] == 1) return false;
			if(map[player.x.intValue()][y.intValue()] >= 50) teleport(map[player.x.intValue()][y.intValue()], player.x.intValue(), y.intValue());
		}
		return true;
	}
	
	public boolean canMoveHorizontally(Double addX) {
		boolean sortie = true;
		
		if(addX > 0) {
			Double x = player.x + 0.495;
			x+=addX;
			if(map[x.intValue()][player.y.intValue()] == 1) sortie = false;
			Double topLeft = player.y - 0.495;
			if(map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = player.y + 0.495;
			if(map[x.intValue()][topRight.intValue()] == 1) sortie = false;
			if(map[x.intValue()][player.y.intValue()] >= 50) teleport(map[x.intValue()][player.y.intValue()], x.intValue(), player.y.intValue());
		}else {
			Double x = (Double)player.x - 0.495;
			x+=addX;
			if(map[x.intValue()][player.y.intValue()] == 1) sortie = false;
			Double topLeft = player.y - 0.495;
			if(map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = player.y + 0.495;
			if(map[x.intValue()][topRight.intValue()] == 1) sortie = false;
			
			if(map[x.intValue()][player.y.intValue()] >= 50) teleport(map[x.intValue()][player.y.intValue()], x.intValue(), player.y.intValue());
		}
		return sortie;
	}
	
	public void teleport(int id, int x, int y) {
		System.out.println("TELEPORT EXCEPT "+x+" "+y);
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[i].length; j++) {
				if(map[i][j] == id && (i != x || j != y)) {
					System.out.println("find "+i+" "+j);
					player.x = i+0.5;
					player.y = j+0.5;
				}
			}
		}
	}
}
