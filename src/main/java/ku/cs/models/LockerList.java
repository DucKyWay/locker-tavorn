package ku.cs.models;

import java.util.ArrayList;

public class LockerList {
    private ArrayList<Locker> lockers;
    public LockerList() { lockers = new ArrayList<>(); }

    public void addLocker(LockerType type, String zone) {
        zone = zone.trim();
        if(!zone.isEmpty()) {
            lockers.add(new Locker(type, zone));
        }
    }

    public void deleteLocker(LockerType type, String zone) {
        lockers.removeIf(l -> l.getType().equals(type) && l.getZone().equals(zone));
    }

    public Locker findLockerByZone(String zone) {
        for (Locker l : lockers) {
            if (l.getZone().equals(zone)) {
                return l;
            }
        }
        return null;
    }

    public Locker findLockerByType(LockerType type) {
        for (Locker l : lockers) {
            if (l.getType().equals(type)) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Locker> getLockers() {
        return lockers;
    }
}
