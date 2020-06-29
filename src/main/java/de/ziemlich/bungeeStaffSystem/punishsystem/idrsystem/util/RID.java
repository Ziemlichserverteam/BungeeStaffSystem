package de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util;

import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;

public class RID {


    private int id;
    private String reason;
    private Type type;
    private String length;

    public RID(int id, String length, String reason, Type type) {
        this.id = id;
        this.reason = reason;
        this.type = type;
        this.length = length;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
