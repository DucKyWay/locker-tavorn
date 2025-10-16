package ku.cs.models.locker;

import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LockerList {
    private ArrayList<Locker> lockers;

    public LockerList() { lockers = new ArrayList<>(); }

    public int genId() {
        if (lockers.isEmpty()) return 1;
        return lockers.getLast().getLockerId() + 1; // ปลอดภัยกว่า getLast()
    }

    public void sortByComparator(Comparator<Locker> comparator){
        Collections.sort(lockers, comparator);
    }

    public void addLocker(Locker locker) {
        if(hasLockerByUid(locker.getLockerUid())) {
            System.out.println(locker.getLockerUid() + " Locker already exists");
            return;
        }
        locker.setLockerId(genId());
        lockers.add(locker);
    }

    public void addLocker(List<Locker> lockers_in) {
        for (Locker locker : lockers_in) {
            if (hasLockerByUid(locker.getLockerUid())) {
                System.out.println(locker.getLockerUid() + " Locker already exists");
                continue;
            }
            locker.setLockerId(genId());
            lockers.add(locker);
        }
    }

    public boolean hasLockerByUid(String lockerUid) {
        return lockers.stream().anyMatch(l -> l.getLockerUid().equals(lockerUid));
    }

    public void deleteLocker(Locker locker) {
        lockers.remove(locker);
    }

    public Locker findLockerByUid(String uid) {
        for (Locker l : lockers) {
            if (l.getLockerUid().equals(uid)) {
                return l;
            }
        }
        return null;
    }

    public LockerList filterByZoneUids(List<String> zoneUids) {
        LockerList result = new LockerList();
        for (Locker locker : lockers) {
            String zoneUid = locker.getZoneUid();
            if (zoneUid == null) continue;
            for (String uid : zoneUids) {
                if (uid != null && uid.trim().equalsIgnoreCase(zoneUid.trim())) {
                    result.addLocker(locker);
                    break;
                }
            }
        }
        return result;
    }

    public int getAllAvailableNow(){
        int i = 0;
        for(Locker l : lockers){
            if(l.isAvailable()){
                i++;
            }
        }
        return i;
    }


    public int getAllAvailable(){
        int i = 0;
        for(Locker l : lockers){
            if(l.isStatus()){
                i++;
            }
        }
        return i;
    }
    public ZoneStatus getStatus(){
        if(lockers.size() == 0){
            return ZoneStatus.INACTIVE;
        }
        else if(getAllAvailableNow()>0){
            return ZoneStatus.ACTIVE;
        }
        else{
            return ZoneStatus.FULL;
        }
    }
    public String getZoneUid(){
        if(lockers.size() == 0){
            return null;
        }
        else{
            return lockers.get(0).getZoneUid();
        }

    }

    public int getCount() {
        return lockers.size();
    }

    public ArrayList<Locker> getLockers() {
        return lockers;
    }
}