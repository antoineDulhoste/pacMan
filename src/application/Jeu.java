package application;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import PathFinding.PathMap;
import javafx.application.Platform;
import javafx.scene.shape.Circle;

public class Jeu extends Observable{
	
	public Level level;
	public final int size = 16;
	public PacMan player;
	public Blinky blinky;
	public Pinky pinky;
	
	public int score = 0;
	Muliplayer multiplayer;
	public PathMap pathMap;
	
	private boolean ready = false;
	private boolean started = false;
	
	public Jeu(Muliplayer multiplayer, Level level) {
		this.multiplayer = multiplayer;
		switch(multiplayer) {
		case SERVER:
			this.level = level;
			serverListen();
			
			this.player = new PacMan(level.getPlayerX()+0.5, level.getPlayerY()+0.5);
			this.pinky = new Pinky(level.getPinkyX()+0.5, level.getPinkyY()+0.5, level.getMap());
			this.blinky = new Blinky(level.getBlinkyX()+0.5, level.getBlinkyY()+0.5, level.getMap());
			this.pathMap = new PathMap(level.map);
			Net.sendData(level.netToString());
			
			viewCall();
			break;
		case CLIENT:
			this.player = new PacMan(0.0, 0.0);
			this.pinky = new Pinky(0.0, 0.0, null);
			this.blinky = new Blinky(0.0, 0.0, null);
			clientListen();
			break;
		case SOLO:
			this.level = level;
			
			this.player = new PacMan(level.getPlayerX()+0.5, level.getPlayerY()+0.5);
			this.pinky = new Pinky(level.getPinkyX()+0.5, level.getPinkyY()+0.5, level.getMap());
			this.blinky = new Blinky(level.getBlinkyX()+0.5, level.getBlinkyY()+0.5, level.getMap());
			this.pathMap = new PathMap(level.map);
			viewCall();
			break;
		}
	}
	
	public void movePlayer(Double x, Double y) {
		this.player.x = x;
		this.player.y = y;
		setChanged();
		notifyObservers();
	}
	
	public void movePlayerX(Double x) {
		this.player.x += x;
		setChanged();
		notifyObservers();
	}
	public void movePlayerY(Double y) {
		this.player.y += y;
		setChanged();
		notifyObservers();
	}
	
	public List<Gum> gums = new ArrayList<>();
	public void addGum(Gum gum) {
		this.gums.add(gum);
		setChanged();
		notifyObservers(gum);
	}
	
	public void removeGum(Gum gum) {
		score += gum.getValue();
		gums.remove(gum);
		setChanged();
		notifyObservers(gum);
	}
	
	public boolean canMoveVertically(Double addY) {
		if(addY > 0) {
			Double y = player.y + 0.495;
			y+=addY;
			if(level.map[player.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = player.x - 0.495;
			if(level.map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = player.x + 0.495;
			if(level.map[topRight.intValue()][y.intValue()] == 1) return false;
			if(level.map[player.x.intValue()][y.intValue()] >= 50) teleport(level.map[player.x.intValue()][y.intValue()], player.x.intValue(), y.intValue());
		}else {
			Double y = player.y - 0.495;
			y+=addY;
			if(level.map[player.x.intValue()][y.intValue()] == 1) return false;
			Double topLeft = player.x - 0.495;
			if(level.map[topLeft.intValue()][y.intValue()] == 1) return false;
			Double topRight = player.x + 0.495;
			if(level.map[topRight.intValue()][y.intValue()] == 1) return false;
			if(level.map[player.x.intValue()][y.intValue()] >= 50) teleport(level.map[player.x.intValue()][y.intValue()], player.x.intValue(), y.intValue());
		}
		return true;
	}
	
	public boolean canMoveHorizontally(Double addX) {
		boolean sortie = true;
		
		if(addX > 0) {
			Double x = player.x + 0.495;
			x+=addX;
			if(level.map[x.intValue()][player.y.intValue()] == 1) sortie = false;
			Double topLeft = player.y - 0.495;
			if(level.map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = player.y + 0.495;
			if(level.map[x.intValue()][topRight.intValue()] == 1) sortie = false;
			if(level.map[x.intValue()][player.y.intValue()] >= 50) teleport(level.map[x.intValue()][player.y.intValue()], x.intValue(), player.y.intValue());
		}else {
			Double x = (Double)player.x - 0.495;
			x+=addX;
			if(level.map[x.intValue()][player.y.intValue()] == 1) sortie = false;
			Double topLeft = player.y - 0.495;
			if(level.map[x.intValue()][topLeft.intValue()] == 1) sortie = false;
			Double topRight = player.y + 0.495;
			if(level.map[x.intValue()][topRight.intValue()] == 1) sortie = false;
			
			if(level.map[x.intValue()][player.y.intValue()] >= 50) teleport(level.map[x.intValue()][player.y.intValue()], x.intValue(), player.y.intValue());
		}
		return sortie;
	}
	
	public void teleport(int id, int x, int y) {
		System.out.println("TELEPORT EXCEPT "+x+" "+y);
		for(int i=0; i<level.map.length; i++) {
			for(int j=0; j<level.map[i].length; j++) {
				if(level.map[i][j] == id && (i != x || j != y)) {
					System.out.println("find "+i+" "+j);
					player.x = i+0.5;
					player.y = j+0.5;
				}
			}
		}
	}
	
	/* MULTIPLAYER */
	/*
	 * Server = PacMan;
	 * Client = Ghost;
	 * 
	 * Packet 0 : Wait
	 * Packet 1 : Start
	 * Packet 2 : End (Gagnant + Score PacMan)
	 * Packet 3 : Move
	 * Packet 4 : Exit
	 * 
	 * Packet Server: 
	 * 	- Player X
	 * 	- Player Y
	 * 
	 * Packet Client:
	 *  - Ghost X
	 *  - Ghost Y
	 */
	public void ServerReceiveData(String str) {
		String[] list = str.split(";");
		pinky.x = Double.parseDouble(list[0]);
		pinky.y = Double.parseDouble(list[1]);
		setChanged();
		notifyObservers();
	}
	
	public String ServerSendData() {
		return player.x+";"+player.y;
	}
	
	public void ClientReceiveData(String str) {
		String[] list = str.split(";");
		player.x = Double.parseDouble(list[0]);
		player.y = Double.parseDouble(list[1]);
		setChanged();
		notifyObservers();
	}
	
	public String ClientSendData() {
		return player.x+";"+player.y;
	}
	
	private void serverListen() {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					while((Net.dis = new DataInputStream(Net.socket.getInputStream())) != null) {
						String str = Net.dis.readUTF();
						String packet = str.split("/")[0];
						System.out.println("Receive Packet ["+packet+"]");
						if( packet.equalsIgnoreCase("0") ) {
							System.out.println("Packet 0 ignored");
						} else if( packet.equalsIgnoreCase("1") ) {
							System.out.println("Client ready");
							Net.sendData("2/Start");
							started = true;
						} else if( packet.equalsIgnoreCase("2") ) {
							System.out.println("Packet 2 ignored");
						} else if( packet.equalsIgnoreCase("3") ) {
							System.out.println("Client end game. Winner : "+packet);
							started = false;
							setChanged();
							notifyObservers("GAMEOVER");
						} else if( packet.equalsIgnoreCase("4") ) {
							String[] list = str.split("/")[1].split(";");
							pinky.x = Double.parseDouble(list[0]);
							pinky.y = Double.parseDouble(list[1]);
							setChanged();
							notifyObservers("RENDEROTHER");
						}
					}
				}catch (Exception ex) {
					ex.printStackTrace();
				}	
			}
		}).start();
	}
	
	private void clientListen() {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					while((Net.dis = new DataInputStream(Net.socket.getInputStream())) != null) {
						String str = Net.dis.readUTF();
						String packet = str.split("/")[0];
						System.out.println("Receive Packet ["+packet+"]");
						if( packet.equalsIgnoreCase("0") ) {
							level = new Level(str);
							
							player = new PacMan(level.getPlayerX()+0.5, level.getPlayerY()+0.5);
							pinky = new Pinky(level.getPinkyX()+0.5, level.getPinkyY()+0.5, level.getMap());
							blinky = new Blinky(level.getBlinkyX()+0.5, level.getBlinkyY()+0.5, level.getMap());
							pathMap = new PathMap(level.map);
							Net.sendData("1/Ready");
							viewCall();
						} else if( packet.equalsIgnoreCase("1") ) {
							System.out.println("Packet 1 ignored");
						} else if( packet.equalsIgnoreCase("2") ) {
							System.out.println("Game start");
							started = true;
						} else if( packet.equalsIgnoreCase("3") ) {
							System.out.println("Serv end game. Winner : "+packet);
							started = false;
							setChanged();
							notifyObservers("GAMEOVER");
						} else if( packet.equalsIgnoreCase("4") ) {
							String[] list = str.split("/")[1].split(";");
							pinky.x = Double.parseDouble(list[0]);
							pinky.y = Double.parseDouble(list[1]);
							setChanged();
							notifyObservers("RENDEROTHER");
						}
					}
				}catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		}).start();
	}
	
	private Jeu local = this;
	private void viewCall() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				new ViewJeu(local, multiplayer);
			}
		});
		
	}
}
