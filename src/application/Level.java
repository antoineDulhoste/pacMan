package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable{
	
	public static ArrayList<Level> levels = new ArrayList<>();
	
	private String name;
	public int[][] map;
	
	private int playerX;
	private int playerY;
	
	private int blinkyX;
	private int blinkyY;
	
	private int pinkyX;
	private int pinkyY;

	public Level(String name, int[][] map, int playerX, int playerY, int blinkyX, int blinkyY, int pinkyX, int pinkyY) {
		super();
		this.name = name;
		this.map = map;
		this.playerX = playerX;
		this.playerY = playerY;
		this.blinkyX = blinkyX;
		this.blinkyY = blinkyY;
		this.pinkyX = pinkyX;
		this.pinkyY = pinkyY;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Level other = (Level) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String netToString() {
		StringBuilder sb = new StringBuilder("0/");
		sb.append(name+";");
		sb.append(map.length+";");
		sb.append(map[0].length+";");
		for (int l = 0; l < map.length ; l++) {
			for (int c = 0; c < map[l].length; c++) {
				sb.append(map[l][c]+":");
			}
		}
		sb.setLength(sb.length()-1);
		sb.append(";"+playerX+";"+playerY);
		sb.append(";"+pinkyX+";"+pinkyY);
		sb.append(";"+blinkyX+";"+blinkyY);
		return sb.toString();
	}
	
	public Level (String str) {
		String[] list = str.split("/")[1].split(";");
		this.name = list[0];
		this.map = new int[Integer.parseInt(list[1])][Integer.parseInt(list[2])];
		String[] vals = list[3].split(":");
		int nbrCol = Integer.parseInt(list[2]);
		
		for (int l = 0; l < map.length ; l++) {
			for (int c = 0; c < map[l].length; c++) {
				map[l][c] = Integer.parseInt(vals[l*nbrCol+c]);
			}
		}
		this.playerX = Integer.parseInt(list[4]);
		this.playerY = Integer.parseInt(list[5]);
		this.pinkyX = Integer.parseInt(list[6]);
		this.pinkyY = Integer.parseInt(list[7]);
		this.blinkyX = Integer.parseInt(list[8]);
		this.blinkyY = Integer.parseInt(list[9]);
	}

	public static void saveLevels() {
		try
        {
            FileOutputStream fos = new FileOutputStream("sauvegarde");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(levels);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
	}
	public static void loadLevels() {
		 try
	        {
	            FileInputStream fis = new FileInputStream("sauvegarde");
	            ObjectInputStream ois = new ObjectInputStream(fis);
	 
	            levels = (ArrayList<Level>) ois.readObject();
	 
	            ois.close();
	            fis.close();
	        }
	        catch (IOException ioe)
	        {
	            ioe.printStackTrace();
	        }
	        catch (ClassNotFoundException c)
	        {
	            System.out.println("Class not found");
	            c.printStackTrace();  
	        }
		 int[][] map = {  { 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8}, 
						  { 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8}, 
						  { 8, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 8}, 
						  { 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8},							
						  { 8, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 8}, 							
						  { 8, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 8},						
						  { 8, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 8}, 							
						  { 8, 8, 8, 8, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 8, 8, 8, 8},							
						  { 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 2, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1}, 						
						  {69, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0,69},						
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
			Level lvl = new Level("Defaut", map, 15, 10, 1, 7, 14, 2);
			if(levels.isEmpty()) levels.add(lvl);
	}

	@Override
	public String toString() {
		return "Level : "+name;
	}

	public static ArrayList<Level> getLevels() {
		return levels;
	}

	public static void setLevels(ArrayList<Level> levels) {
		Level.levels = levels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getPlayerX() {
		return playerX;
	}

	public void setPlayerX(int playerX) {
		this.playerX = playerX;
	}

	public int getPlayerY() {
		return playerY;
	}

	public void setPlayerY(int playerY) {
		this.playerY = playerY;
	}

	public int getBlinkyX() {
		return blinkyX;
	}

	public void setBlinkyX(int blinkyX) {
		this.blinkyX = blinkyX;
	}

	public int getBlinkyY() {
		return blinkyY;
	}

	public void setBlinkyY(int blinkyY) {
		this.blinkyY = blinkyY;
	}

	public int getPinkyX() {
		return pinkyX;
	}

	public void setPinkyX(int pinkyX) {
		this.pinkyX = pinkyX;
	}

	public int getPinkyY() {
		return pinkyY;
	}

	public void setPinkyY(int pinkyY) {
		this.pinkyY = pinkyY;
	}
	
	public static void clear() {
		levels.clear();
		saveLevels();
		loadLevels();
	}

}
