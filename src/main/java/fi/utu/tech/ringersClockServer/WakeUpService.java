package fi.utu.tech.ringersClockServer;

import java.time.LocalTime;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fi.utu.tech.ringersClock.entities.AlarmConfirm;
import fi.utu.tech.ringersClock.entities.JoinMessage;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;

public class WakeUpService extends Thread {

	private Integer UUID;
	private WakeUpGroup wakeUpGroup;
	private Vector<WakeUpGroup> groups;
	private Vector<WakeUpGroup> groups2;
	private ConcurrentHashMap<Integer, Vector<WakeUpGroup>> groupsWithID = new ConcurrentHashMap<>();
	private ConcurrentHashMap<LocalTime, Vector<WakeUpGroup>> wakeuptimes;
	private Integer timeHour;
	private Integer timeMinutes;
	private LocalTime wakeUpTime;
	private LocalTime timeNow;
	
	public WakeUpService() {

	}

	public void run() {
		//herätysajan vertailu oikeaan aikaan minuutin välein
		
		//kun herätys, looppaisi groups2:n johtajat läpi ja lähettäisi herätysviestin

	}

	public void handleAlarmConfirm(AlarmConfirm obj) {
		if(obj.getWakeUp()) {
			ClientListener.send(obj);
		}
		
	}

public void setGroupIdList(WakeUpGroup wakeUpGroup) {
		UUID = wakeUpGroup.getID();
		groups.add(wakeUpGroup);
		groupsWithID.put(UUID, groups);
		setGroupTimeList(wakeUpGroup);
	}
	
	public void setGroupTimeList(WakeUpGroup wakeUpGroup) {
		timeHour = wakeUpGroup.getHour();
		timeMinutes = wakeUpGroup.getMinutes();
		wakeUpTime = LocalTime.of(timeHour, timeMinutes);
		groups2.add(wakeUpGroup);
		wakeuptimes.put(wakeUpTime, groups2);
	}

	public void handleJoin(ClientListener client, JoinMessage message) {

	}

	public void handleResign(ClientListener client) {

	}
}
