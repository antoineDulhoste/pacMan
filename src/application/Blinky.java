package application;

import java.util.List;

import PathFinding.Node;

public class Blinky extends Ghost {

	public Blinky(double x, double y, Jeu jeu) {
		super(x, y, jeu);
	}

	@Override
	public void run() {	
		try {
			if(jeu.isOver()) return;
			List<Node> nodesBlinky = jeu.pathMap.findPath(jeu.blinky.x.intValue(), jeu.blinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
			if(nodesBlinky != null && !nodesBlinky.isEmpty() && !jeu.isOver()) {
				jeu.moveBlinky(nodesBlinky.get(0).getX()+0.5, nodesBlinky.get(0).getY()+0.5);
			}
		}catch(ArrayIndexOutOfBoundsException aioobe) {
			System.err.println("[WARN] Blinky AIOOBE");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
