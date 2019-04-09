package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;

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
	 * Packet 6 : PacMan Score
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
	 * 
	 * Packet 3 : End
	 * - String winner
	 */
	public static void sendData(String str) {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static Level level = null;
	public static boolean already = false;
	public static void startServer(int port, Level levelArg) {
		if(already) return;
		try {
			System.out.println("Starting Server ...");
			serverSocket = new ServerSocket(port);
			System.out.println("Server Started\nWaiting for level ...");
			
			/** Ask for Level **/
			if(levelArg == null) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						try {
							ChoiceDialog<Level> dialog = new ChoiceDialog<>(Level.levels.get(0), Level.levels);
							dialog.setTitle("Choisir un niveau");
							dialog.setHeaderText("Look, a Choice Dialog");
							dialog.setContentText("Choose your letter:");

							// Traditional way to get the response value.
							Optional<Level> result = dialog.showAndWait();
							if (result.isPresent()){
								level = result.get();
							    System.out.println("Level ("+level.getName()+") OK");
							    System.out.println("Waiting for connection ...");
							    socket = serverSocket.accept();
								System.out.println("Connection from: "+ socket.getInetAddress());
								
								dis = new DataInputStream(socket.getInputStream());	
								if (dis != null && level != null) {
									System.out.println("Server started\nClient connected\nSending game infos");
									new Jeu(Muliplayer.SERVER, level);
								}
							}	
						}catch (Exception ex) {
							ex.printStackTrace();
						}	
					}
				});
			}else {
				try {
				    System.out.println("Level ("+levelArg.getName()+") OK");
				    System.out.println("Waiting for connection ...");
				    socket = serverSocket.accept();
					System.out.println("Connection from: "+ socket.getInetAddress());
					
					dis = new DataInputStream(socket.getInputStream());	
					if (dis != null && levelArg != null) {
						System.out.println("Server started\nClient connected\nSending game infos");
						new Jeu(Muliplayer.SERVER, levelArg);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		already = true;
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
			new Jeu(Muliplayer.CLIENT, null);
		}
		
	}
}
