package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;

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
}
