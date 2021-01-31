package fi.utu.tech.ringersClockServer;

import java.io.IOException;
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
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientListener client = new ClientListener(clientSocket, wup);
				client.start();
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}