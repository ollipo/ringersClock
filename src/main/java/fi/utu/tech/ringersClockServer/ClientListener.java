package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;
import fi.utu.tech.ringersClock.entities.JoinMessage;
import fi.utu.tech.ringersClock.entities.ResignMessage;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;

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
	private ObjectOutputStream oOut;

	public ClientListener(Socket s, WakeUpService wup) {
		client = s;
		this.wup = wup;
	}

	public void run() {
		try {
			InputStream iS = client.getInputStream();
			OutputStream oS = client.getOutputStream();
			oOut = new ObjectOutputStream(oS);
			ObjectInputStream oIn = new ObjectInputStream(iS);
			while(true) {
				Object obj = oIn.readObject();
				if (obj instanceof AlarmConfirm) {
					wup.handleAlarmConfirm((AlarmConfirm) obj);
				}
				if (obj instanceof JoinMessage) {
					wup.handleJoin(this, (JoinMessage) obj);
				}
				if (obj instanceof ResignMessage) {
					wup.handleResign(this);
				}
				if (obj instanceof WakeUpGroup) {
					wup.handleNewGroup((WakeUpGroup)obj, this);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void send(Serializable s) throws java.io.IOException {
			oOut.writeObject(s);
			oOut.flush();
			oOut.close();
	}
}