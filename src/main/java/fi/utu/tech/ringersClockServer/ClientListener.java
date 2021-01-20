package fi.utu.tech.ringersClockServer;

import java.net.Socket;

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
				Object obj = ois.readObject();
				if (obj instanceof AlarmConfirm) {
				  wup.handleAlarmConfirm((AlarmConfirm)obj);
				  
					oOut.writeObject(obj);
					oOut.flush();
				}
			} catch (IOException e) {
				oIn.close();
				oOut.close();
			}
			} catch (Exception e) {
				throw new Error(e.toString());
			}
	}
}