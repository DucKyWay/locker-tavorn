package ku.cs.models.zone;

import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;

import java.util.ArrayList;

public class Zone {
    private String zone;
    private int idZone;
    private LockerList lockers;
    public Zone() {
    }
    public Zone(String label, int idZone) {
        this.zone = label;
        this.idZone = idZone;
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
    public void addLockerList(LockerList lockers) {
        this.lockers = lockers;
    }
    public LockerList getLockerList() {
        return lockers;
    }
    @Override
    public String toString() {
        return zone;
    }
    public void setIdZone(int i) {
        this.idZone = i;
    }
}
