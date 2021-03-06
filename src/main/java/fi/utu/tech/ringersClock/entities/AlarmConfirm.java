package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

/*
 * Entity class presenting an AlarmConfirm.
 */

public class AlarmConfirm implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean wakeUp;
	private WakeUpGroup wug;

	public AlarmConfirm(WakeUpGroup wug, boolean wakeUp) {
		this.wug = wug;
		this.wakeUp = wakeUp;
	}

	public AlarmConfirm(WakeUpGroup wug) {
		this.wug = wug;
	}

	public WakeUpGroup getWakeUpGroup() {
		return this.wug;
	}

	public boolean getWakeUp() {
		return this.wakeUp;
	}

	public void setWakeUp(boolean wakeUp) {
		this.wakeUp = wakeUp;
	}

}
