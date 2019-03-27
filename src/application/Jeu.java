package application;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import PathFinding.PathMap;
import javafx.application.Platform;
import javafx.scene.shape.Circle;

public class Jeu extends Observable{
	
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
	
	public int score = 0;
	Muliplayer multiplayer;
	public PathMap pathMap;
	
	private boolean ready = false;
	private boolean started = false;
	
	public Jeu() {
		this.player = new PacMan(15.5, 10.5);
		this.blinky = new Blinky(1.5, 7.5, map);
		this.pinky = new Pinky(14.5, 2.5, map);
		this.pathMap = new PathMap(map);
	}
	
	public Jeu(Muliplayer multiplayer) {
		this.multiplayer = multiplayer;
		switch(multiplayer) {
		case SERVER:
			serverListen();
			
			this.player = new PacMan(15.5, 10.5);
			this.pinky = new Pinky(14.5, 2.5, map);
			this.blinky = new Blinky(1.5, 7.5, map);
			
			StringBuilder sb = new StringBuilder("0/");
			sb.append(map.length+";");
			sb.append(map[0].length+";");
			for (int l = 0; l < map.length ; l++) {
				for (int c = 0; c < map[l].length; c++) {
					sb.append(map[l][c]+":");
				}
			}
			sb.setLength(sb.length()-1);
			sb.append(";"+player.x+";"+player.y);
			sb.append(";"+pinky.x+";"+pinky.y);
			sb.append(";"+blinky.x+";"+blinky.y);
			Net.sendData(sb.toString());
			viewCall();
			break;
		case CLIENT:
			this.player = new PacMan(15.5, 10.5);
			this.pinky = new Pinky(14.5, 2.5, map);
			this.blinky = new Blinky(1.5, 7.5, map);
			clientListen();
			break;
		case SOLO:
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
						} else if( packet.equalsIgnoreCase("4") ) {
							String[] list = str.split("/")[1].split(";");
							pinky.x = Double.parseDouble(list[0]);
							pinky.y = Double.parseDouble(list[1]);
							setChanged();
							notifyObservers("RENDEROTHER");
						}
						//ClientReceiveData(str);
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
							String[] list = str.split("/")[1].split(";");
							map = new int[Integer.parseInt(list[0])][Integer.parseInt(list[1])];
							String[] vals = list[2].split(":");
							int nbrCol = Integer.parseInt(list[1]);
							
							for (int l = 0; l < map.length ; l++) {
								for (int c = 0; c < map[l].length; c++) {
									map[l][c] = Integer.parseInt(vals[l*nbrCol+c]);
								}
							}
							player.x = Double.parseDouble(list[3]);
							player.y = Double.parseDouble(list[4]);
							pinky.x = Double.parseDouble(list[5]);
							pinky.y = Double.parseDouble(list[6]);
							blinky.x = Double.parseDouble(list[7]);
							blinky.y = Double.parseDouble(list[8]);
							blinky.plateau = map;
							pinky.plateau = map;
							pathMap = new PathMap(map);
							Net.sendData("1/Ready");
							viewCall();
						} else if( packet.equalsIgnoreCase("1") ) {
							System.out.println("Packet 1 ignored");
						} else if( packet.equalsIgnoreCase("2") ) {
							System.out.println("Game start");
							started = true;
						} else if( packet.equalsIgnoreCase("4") ) {
							String[] list = str.split("/")[1].split(";");
							pinky.x = Double.parseDouble(list[0]);
							pinky.y = Double.parseDouble(list[1]);
							setChanged();
							notifyObservers("RENDEROTHER");
						}
						//ClientReceiveData(str);
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
