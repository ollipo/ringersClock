package fi.utu.tech.ringersClockServer;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketListener extends Thread {

	private String host;
	private int port;
	private WakeUpService wup;

	public ServerSocketListener(String host, int port, WakeUpService wup) {
		this.host = host;
		this.port = port;
		this.wup = wup;

	}

	public void run() {
		ServerSocket serverSocket = new ServerSocket(port);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			new ClientListener(clientSocket, wup);
		}
		
	}
}
