package ku.cs.models.zone;

import ku.cs.services.utils.UuidUtil;

public class Zone {
    private String zoneUid;
    private int idZone = 0;
    private String zone = ""; //set default to empty string
    private int totalLocker = 0;
    private int totalAvailableNow = 0;
    private int totalAvailable = 0;
    private int totalUnavailable;
    private ZoneStatus status = ZoneStatus.INACTIVE;

    public Zone() {
        this.zoneUid = UuidUtil.generateShort();
    }

    public Zone(String label, int idZone) {
        this.zone = label;
        this.idZone = idZone;
        this.zoneUid = UuidUtil.generateShort();
    }

    public String getZoneUid() {
        return zoneUid;
    }
    public void setZoneUid(String zoneUid) { this.zoneUid = zoneUid; }

    public void setTotalLocker(int totalLocker) {
        this.totalLocker = totalLocker;
    }

    public void setTotalAvailableNow(int totalAvailableNow) {
        this.totalAvailableNow = totalAvailableNow;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public int getTotalLocker() {
        return totalLocker;
    }

    public int getTotalAvailableNow() {
        return totalAvailableNow;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }
    public ZoneStatus getStatus() {
        return status;
    }
    public void setStatus(ZoneStatus status) {
        this.status = status;
    }
    public void toggleStatus() {
        switch (status) {
            case INACTIVE:
                status = ZoneStatus.ACTIVE;
                break;
            case ACTIVE:
                status = ZoneStatus.FULL;
                break;
            case  FULL:
                status = ZoneStatus.INACTIVE;
                break;
        }
    }

    public void setZone(String label){
        this.zone = label;
    }
    public String getZone() {
        return zone;
    }
    public int getIdZone() {
        return idZone;
    }
    public void setIdZone(int i) {
        this.idZone = i;
    }

    public int getTotalUnavailable() {
        return totalLocker - totalAvailable;
    }

    @Override
    public String toString() {
        return zone;
    }
}
