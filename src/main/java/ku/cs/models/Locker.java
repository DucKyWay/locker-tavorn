package ku.cs.models;

import java.sql.Timestamp;

public class Locker {
    private static int counter = 0;
    private int id;
    private LockerType type;
    private String zone;
    private boolean available;
    private boolean status;
    private Timestamp startTime;
    private Timestamp endTime;

    public Locker(LockerType type, String zone) {
        id = ++counter;
        this.type = type;
        this.zone = zone;
        this.available = true;
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public LockerType getType() {
        return type;
    }

    public void setType(LockerType type) {
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        this.startTime = new Timestamp(System.currentTimeMillis());
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        this.endTime = new Timestamp(System.currentTimeMillis());
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}