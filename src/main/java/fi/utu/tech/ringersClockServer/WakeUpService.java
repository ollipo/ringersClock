package fi.utu.tech.ringersClockServer;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map;
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

	private ConcurrentHashMap<Integer, Vector<ClientListener>> clientsByGroupId = new ConcurrentHashMap<>();
	
	public WakeUpService() {

	}

	public void run() {
		//herätysajan vertailu oikeaan aikaan minuutin välein
		
		//kun herätys, looppaisi groups2:n johtajat läpi ja lähettäisi herätysviestin

	}

	public void handleAlarmConfirm(AlarmConfirm obj) {
		try {
			if (obj.getWakeUp()) {
				ClientListener.send(obj);
			}
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
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

	// Adds client to list if not yet a member of any groups
	public void handleJoin(ClientListener client, JoinMessage message) {
		Integer groupId = message.getWakeUpGroup().getID();
		if(checkIfMember(client)) {
			clientsByGroupId.get(groupId).add(client);
			message.setJoinSucceeded(true);
			try {
				ClientListener.send(message);
			} catch(IOException ex) {
				System.out.println(ex.getMessage());
			}
		} else {
			message.setJoinSucceeded(false);
			try {
				ClientListener.send(message);
			} catch(IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}

	// Resigns client from group
	public void handleResign(ClientListener client) {
		for(Map.Entry<Integer, Vector<ClientListener>> entry : clientsByGroupId.entrySet()) {
			entry.getValue().remove(client);
		}
	}

	// Checks if client already in a group
	public boolean checkIfMember(ClientListener client) {
		boolean success = true;
		for(Map.Entry<Integer, Vector<ClientListener>> entry : clientsByGroupId.entrySet()) {
			if(entry.getValue().contains(client)) {
				success = false;
			}
			else {
				success = true;

			}
		}
		return success;
	}

}
