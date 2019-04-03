package application;

import java.util.List;

import PathFinding.Node;

public class Pinky extends Ghost {

	public Pinky(double x, double y, Jeu jeu) {
		super(x, y, jeu);
	}

	@Override
	public void run() {
		try {
			if(jeu.isServer() || jeu.isClient() || jeu.isOver()) return;
				List<Node> nodesPinky;
				try {
		    		switch (jeu.player.dir) {
		        	case 1: 
		        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue() - 3);
		        		break;
		        	case 2: 
		        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue() + 3, jeu.player.y.intValue());
		        		break;
		        	case 3: 
		        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue() + 3);
		        		break;
		        	case 4: 
		        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue() - 3, jeu.player.y.intValue());
		        		break;
		        	default:
		        		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
		        		break;
		        	}
		    	}catch(Exception ex) {
		    		nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
		    	}
				if(nodesPinky == null || nodesPinky.isEmpty()) {
		 			nodesPinky = jeu.pathMap.findPath(jeu.pinky.x.intValue(), jeu.pinky.y.intValue(), jeu.player.x.intValue(), jeu.player.y.intValue());
		 		}
				if(nodesPinky != null && !nodesPinky.isEmpty() && !jeu.isOver()) {
					jeu.movePinky(nodesPinky.get(0).getX()+0.5, nodesPinky.get(0).getY()+0.5);
				}
				//sleep(250);
			//}
		}catch(ArrayIndexOutOfBoundsException aioobe) {
			System.err.println("[WARN] Pinky AIOOBE");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
