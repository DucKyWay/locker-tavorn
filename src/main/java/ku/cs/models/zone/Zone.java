package ku.cs.models.zone;

import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;

import java.util.ArrayList;

public class Zone {
    private String zone = ""; //set default to empty string
    private int idZone = 0;
    private int totalLocker = 0;
    private int totalAvailableStatus = 0;
    private int totalAvailable = 0;
    private String status = "";
    public Zone() {
    }
    public Zone(String label, int idZone) {
        this.zone = label;
        this.idZone = idZone;

    }

    public void setTotalLocker(int totalLocker) {
        this.totalLocker = totalLocker;
    }

    public void setTotalAvailableStatus(int totalAvailableStatus) {
        this.totalAvailableStatus = totalAvailableStatus;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public int getTotalLocker() {
        return totalLocker;
    }

    public int getTotalAvailableStatus() {
        return totalAvailableStatus;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    @Override
    public String toString() {
        return zone;
    }
    public void setIdZone(int i) {
        this.idZone = i;
    }
}
