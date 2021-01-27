package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

/*
 * Entity class presenting a WakeUpGroup. The class is not complete.
 * You need to add some variables.
 */

public class WakeUpGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String ID;
	private Integer hour;
	private Integer minutes;
	private boolean norain;
	private boolean temp;
	

	public WakeUpGroup(String ID, String name, Integer hour, Integer minutes, boolean norain, boolean temp) {
		super();
		this.ID = ID;
		this.name = name;
		this.hour = hour;
		this.minutes = minutes;
		this.norain = norain;
		this.temp = temp;
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}
	
	public Integer getHour() {
		return this.hour;
	}
	
	public Integer getMinutes() {
		return this.minutes;
	}
	
	public boolean getRain() {
		return this.norain;
	}
	
	public boolean getTemp() {
		return this.temp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setID(String ID) {
		this.ID = ID;
	}
	
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	
	public void setRain(boolean norain) {
		this.norain = norain;
	}
	
	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
