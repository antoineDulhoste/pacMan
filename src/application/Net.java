package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Net {
	
	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream dos;
	static DataInputStream dis;
	
	/**
	 * Packet 0 : Games infos
	 * Packet 1 : Ready to start
	 * Packet 2 : Start
	 * Packet 3 : End
	 * Packet 4 : Move
	 * Packet 5 : Blinky Position
	 * 
	 * Packet 0 : Map datas
	 * - int lenght
	 * - int width
	 * - int[][] map
	 * - double playerX
	 * - double playerY
	 * - double blinkyX
	 * - double blinkyY
	 * - double pinkyX
	 * - double pinkyY
	 */
	public static void sendData(String str) {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void startServer() {
		try {
			System.out.println("Starting Server ...");
			serverSocket = new ServerSocket(7778);
			System.out.println("Server Started\nWaiting for connection ...");
			socket = serverSocket.accept();
			System.out.println("Connection from: "+ socket.getInetAddress());
			
			dis = new DataInputStream(socket.getInputStream());	
		}catch(Exception ex) {
			
		}
		if (dis != null) {
			System.out.println("Server started\nClient connected\nSending game infos");
			new Jeu(Muliplayer.SERVER);
		}
		
	}

	public static void joinServer(String ip, int port) {
		System.out.println("Connection to server");
		try {
			socket = new Socket(ip, port);
			dis = new DataInputStream(socket.getInputStream());
		}catch(Exception ex) {
			System.out.println("UnknownHostException: "+ip+":"+port);
		}
		if (dis != null) {
			System.out.println("Client started\nConnexion valid\nWaiting for Game infos");
			new Jeu(Muliplayer.CLIENT);
		}
		
	}
}
