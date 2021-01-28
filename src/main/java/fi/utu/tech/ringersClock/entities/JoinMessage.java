package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

public class JoinMessage implements Serializable {

    private static final long serialVersionUID = 2L;
    private WakeUpGroup group;
    boolean joinSucceeded;

    public JoinMessage(WakeUpGroup group) {

        this.group = group;
    }

    public WakeUpGroup getWakeUpGroup() {
        return group;
    }

    public void setWakeUpGroup(WakeUpGroup group) {
        this.group = group;
    }

    public void setJoinSucceeded(boolean success) {
        this.joinSucceeded = success;
    }

    public boolean getJoinSucceeded() {
        return joinSucceeded;
    }

}
