package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;
import fi.utu.tech.ringersClock.entities.JoinMessage;
import fi.utu.tech.ringersClock.entities.ResignMessage;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;


public class ClientListener extends Thread {

	private Socket client;
	private WakeUpService wup;

	public ClientListener(Socket s, WakeUpService wup) {
		client = s;
		this.wup = wup;
	}

	public void run() {
		try {
			InputStream iS = client.getInputStream();
			OutputStream oS = client.getOutputStream();
			ObjectOutputStream oOut = new ObjectOutputStream(oS);
			ObjectInputStream oIn = new ObjectInputStream(iS);
			try {
				Object obj = oIn.readObject();
				if (obj instanceof AlarmConfirm) {
				  wup.handleAlarmConfirm((AlarmConfirm)obj);
				}
				if (obj instanceof JoinMessage) {
					wup.handleJoin(this, (JoinMessage)obj);
				}
				if (obj instanceof ResignMessage) {
					wup.handleResign(this);
				}
				
			} catch (IOException e) {
				oIn.close();
		} catch (Exception e) {
			throw new Error(e.toString());
			}
		}
	}
		
	public static void send(Serializable s) {
		try {
			oOut.writeObject(s);
			oOut.flush();
		} catch (IOException e) {
			oOut.close();
		} catch (Exception e) {
				throw new Error(e.toString());
		}
	}
}