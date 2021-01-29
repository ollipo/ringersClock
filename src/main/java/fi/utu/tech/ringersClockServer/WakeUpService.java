package fi.utu.tech.ringersClockServer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fi.utu.tech.ringersClock.entities.*;
import fi.utu.tech.weatherInfo.FMIWeatherService;
import fi.utu.tech.weatherInfo.WeatherData;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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

	private ConcurrentHashMap<Integer, Vector<ClientListener>> clientsByGroupId = new ConcurrentHashMap<>();
	private DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
	
	public WakeUpService() {

	}

	public void run() {
		//herätysajan vertailu oikeaan aikaan minuutin välein
		
		//kun herätys, looppaisi groups2:n johtajat läpi ja lähettäisi herätysviestin

		while(true) {
			try {
				// Variable for time right now
				LocalTime timeNow = LocalTime.now();
				// Format to string
				String timeNowAsString = DATE_TIME_FORMAT.format(timeNow);

				// Loop through wakeuptimes, see if key matches timeNowAsString
				for(Map.Entry<LocalTime, Vector<WakeUpGroup>> entry : wakeuptimes.entrySet()) {
					String wakeuptime = entry.getKey().toString();
					// If key matches, parse time as string back to LocalTime and create a vector for all groups in key
					if(wakeuptime.equals(timeNowAsString)) {
						LocalTime time = LocalTime.parse(wakeuptime);
						Vector<WakeUpGroup> groupsInKey = new Vector<>(wakeuptimes.get(time));
						// Loop through created group vector, check if weatherdata okay, and send alarmConfirm to leader
						// How to handle cancel? Need to destroy group, but first need to update grouplist in client?
						for(WakeUpGroup g : groupsInKey) {
							if (checkWeather(g)) {
								AlarmConfirm confirm = new AlarmConfirm(g);
								try {
									clientsByGroupId.get(g.getID()).get(0).send(confirm);
								} catch (IOException ex) {
									System.out.println(ex.getMessage());
								}
							}
						}
					}
				}
				// Sleep for one minute, then repeat
				sleep(60000);
			} catch(InterruptedException ex) {
				System.out.println(ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}

	}

	// Sends alarm confirmed or cancelled message to all members of group
	public void handleAlarmConfirm(AlarmConfirm obj) {
		try {
			if (obj.getWakeUp()) {
				// Check groups ID and save all clients from clientsByGroupId to a vector
				Vector<ClientListener> clients = new Vector<>(clientsByGroupId.get(obj.getWakeUpGroup().getID()));
				// Loop through vector and send message to alarm all clients
				// How to handle cancel? Need to destroy group, but first need to update grouplist in client
				for(ClientListener c : clients) {
					AlarmMessage alarmConfirmed = new AlarmMessage();
					c.send(alarmConfirmed);
				}
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

	// Adds new group to list and sends all groups back to client for update
	public void handleNewGroup(WakeUpGroup group, ClientListener client) {
		// Set group and time to wakeuptimes
		setGroupTimeList(group);
		// Create new client vector, add leader to vector, and add vector to clientsByGroupId
		Vector<ClientListener> clients = new Vector<>();
		clients.add(client);
		clientsByGroupId.put(group.getID(), clients);

		// Send all groups to client to update grouplist
		try {
			client.send(getGroupsAsArray());
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}

	}

	// Gathers groups into ArrayList
	public ArrayList<WakeUpGroup> getGroupsAsArray() {
		ArrayList<WakeUpGroup> groupsInArrayList = new ArrayList<>();
		for(Map.Entry<LocalTime, Vector<WakeUpGroup>> entry : wakeuptimes.entrySet()) {
			groupsInArrayList.addAll(entry.getValue());
		}
		return groupsInArrayList;
	}


	// Adds client to list if not yet a member of any groups
	public void handleJoin(ClientListener client, JoinMessage message) {
		Integer groupId = message.getWakeUpGroup().getID();
		if(checkIfMember(client)) {
			clientsByGroupId.get(groupId).add(client);
			message.setJoinSucceeded(true);
			try {
				client.send(message);
			} catch(IOException ex) {
				System.out.println(ex.getMessage());
			}
		} else {
			message.setJoinSucceeded(false);
			try {
				client.send(message);
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

	//Checks if current weather matches group selection
	public boolean checkWeather(WakeUpGroup group) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
		boolean weatherOkay = true;
		WeatherData data = FMIWeatherService.getWeather();
		if(group.getRain()) {
			if(data.isRaining()) {
				weatherOkay = false;
			}
		}
		if(group.getTemp()) {
			if(data.isBelowZero()) {
				weatherOkay = false;
			}
		}
		return weatherOkay;
	}

}
