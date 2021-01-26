package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;
import fi.utu.tech.ringersClock.entities.JoinMessage;

public class WakeUpService extends Thread {

	public WakeUpService() {

	}

	public void run() {

	}

	public void handleAlarmConfirm(AlarmConfirm obj) {
		if(obj.getWakeUp()) {
			ClientListener.send(obj);
		}
		
	}

	public void handleJoin(ClientListener client, JoinMessage message) {

	}

	public void handleResign(ClientListener client) {

	}
}
