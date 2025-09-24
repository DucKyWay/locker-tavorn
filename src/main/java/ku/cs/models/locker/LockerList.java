package ku.cs.models.locker;

import java.util.ArrayList;

public class LockerList {
    private ArrayList<Locker> lockers;
    public LockerList() { lockers = new ArrayList<>(); }

    public void addLocker(LockerType type, String zone) {
        zone = zone.trim();
        if(!zone.isEmpty()) {
            lockers.add(new Locker(lockers.size(),type, zone));
        }
    }
    public void genId(){
        int i = 0;
        for(Locker l : lockers){
            l.setId(i);
            i++;
        }
    }
    public void addLocker(Locker locker) {
        lockers.add(locker);
    }

    public void deleteLocker(LockerType type, String zone) {
        lockers.removeIf(l -> l.getLockerType().equals(type) && l.getZone().equals(zone));
    }

    public Locker findLockerByZone(String zone) {
        for (Locker l : lockers) {
            if (l.getZone().equals(zone)) {
                return l;
            }
        }
        return null;
    }
    public Locker findLockerByUuid(String uuid) {
        for (Locker l : lockers) {
            if (l.getUuid().equals(uuid)) {
                return l;
            }
        }
        return null;
    }
    public Locker findLockerByType(LockerType type) {
        for (Locker l : lockers) {
            if (l.getLockerType().equals(type)) {
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
    public int getAllAvalibleNow(){
        int i = 0;
        for(Locker l : lockers){
            if(l.getStatus() == true){
                i++;
            }
        }
        return i;
    }


    public int getAllAvailable(){
        int i = 0;
        for(Locker l : lockers){
            if(l.getAvailable() == true){
                i++;
            }
        }
        return i;
    }
    public String getStatusString(){
        if(lockers.size() == 0){
            return "Not Active";
        }
        else if(getAllAvalibleNow()>0){
            return "Active";
        }
        else{
            return "Full";
        }
    }
    public ArrayList<Locker> getLockers() {
        return lockers;
    }
}