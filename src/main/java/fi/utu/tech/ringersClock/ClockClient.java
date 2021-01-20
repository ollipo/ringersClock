package fi.utu.tech.ringersClock;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * A class for handling network related stuff
 */

public class ClockClient extends Thread {

	private String host;
	private int port;
	private Gui_IO gio;
	private ObjectOutputStream oOut;

	public ClockClient(String host, int port, Gui_IO gio) {
		this.host = host;
		this.port = port;
		this.gio = gio;
	}

	public void run() {
		System.out.println("Host name: " + host + " Port: " + port + " Gui_IO:" + gio.toString());
		Socket s = new Socket(host, port);
		
		InputStream iS = s.getInputStream();
		OutputStream oS = s.getOutputStream();
		
		oOut = new ObjectOutputStream(oS);
		ObjectInputStream oIn = new ObjectInputStream(iS);
		
		try {
			Object obj = oIn.readObject();
			if (obj instanceof AlarmConfirm) {
			  gio.alarm();
			}
		} catch (IOException e) {
			oIn.close();
			s.close();
			} catch (Exception e) {
			throw new Error(e.toString());
			}
		}
	
	public void send(Serializable s) throws java.io.IOException {
		oOut.writeObject(s);
		oOut.flush();
		oOut.close();
		}

}
