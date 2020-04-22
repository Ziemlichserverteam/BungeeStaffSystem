package de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util;

import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;

public class RID {


    private int id;
    private String reason;
    private Type type;

    public RID(int id, String reason, Type type) {
        this.id = id;
        this.reason = reason;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
