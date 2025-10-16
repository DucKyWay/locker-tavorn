package ku.cs.models.zone;

import ku.cs.services.utils.UuidUtil;

public class Zone {
    private String zoneUid;
    private int zoneId = 0;
    private String zoneName; //set default to empty string
    private int totalLocker = 0;
    private int totalAvailableNow = 0;
    private int totalAvailable = 0;
    private final int totalUnavailable = 0;
    private ZoneStatus status = ZoneStatus.INACTIVE;

    public Zone() {
        this.zoneUid = new UuidUtil().generateShort();
    }

    public Zone(String label, int zoneId) {
        this.zoneUid = new UuidUtil().generateShort();
        this.zoneId = zoneId;
        this.zoneName = label;
    }

    public String getZoneUid() {
        return zoneUid;
    }

    public void setZoneUid(String zoneUid) { this.zoneUid = zoneUid; }

    public void setZoneName(String label){
        this.zoneName = label;
    }

    public String getZoneName() {
        return zoneName;
    }

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

    public int getTotalUnavailable() {
        return totalLocker - totalAvailableNow;
    }

    public ZoneStatus getStatus() {
        return status;
    }

    public void setStatus(ZoneStatus status) {
        this.status = status;
    }

    public void toggleStatus() {
        if (status == ZoneStatus.INACTIVE) {
            status = ZoneStatus.ACTIVE;
        } else if (status == ZoneStatus.ACTIVE) {
            if (getTotalLocker() != 0 && getTotalAvailableNow() == 0) {
                status = ZoneStatus.FULL;
            } else {
                status = ZoneStatus.INACTIVE;
            }
        } else if (status == ZoneStatus.FULL) {
            status = ZoneStatus.ACTIVE;
        }
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int i) {
        this.zoneId = i;
    }

    @Override
    public String toString() {
        return zoneName;
    }
}
