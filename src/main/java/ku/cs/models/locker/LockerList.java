package ku.cs.models.locker;

import java.util.ArrayList;

public class LockerList {
    private ArrayList<Locker> lockers;
    public LockerList() { lockers = new ArrayList<>(); }

    public void addLocker(KeyType type, String zone) {
        zone = zone.trim();
        if(!zone.isEmpty()) {
            lockers.add(new Locker(type, zone));
        }
    }
    public void addLocker(Locker locker) {
        lockers.add(locker);
    }

    public void deleteLocker(KeyType type, String zone) {
        lockers.removeIf(l -> l.getKeyType().equals(type) && l.getZone().equals(zone));
    }

    public Locker findLockerByZone(String zone) {
        for (Locker l : lockers) {
            if (l.getZone().equals(zone)) {
                return l;
            }
        }
        return null;
    }

    public Locker findLockerByType(KeyType type) {
        for (Locker l : lockers) {
            if (l.getKeyType().equals(type)) {
                return l;
            }
        }
        return null;
    }

    public Locker findLockerByAvailable(boolean available) {
        for (Locker l : lockers) {
            if (l.getAvailable() == available) {
                return l;
            }
        }
        return null;
    }

    public Locker findLockerByStatus(boolean status) {
        for (Locker l : lockers) {
            if (l.getStatus() == status) {
                return l;
            }
        }
        return null;
    }
    public int getAllAvalible(){
        int i = 0;
        for(Locker l : lockers){
            if(l.getAvailable() == true){
                i++;
            }
        }
        return i;
    }

    public int getAllUnavailable(){
        int i = 0;
        for(Locker l : lockers){
            if(l.getAvailable() == false){
                i++;
            }
        }
        return i;
    }
    public ArrayList<Locker> getLockers() {
        return lockers;
    }
}
