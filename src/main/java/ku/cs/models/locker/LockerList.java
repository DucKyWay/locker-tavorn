package ku.cs.models.locker;

import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;
import java.util.List;

public class LockerList {
    private ArrayList<Locker> lockers;
    public LockerList() { lockers = new ArrayList<>(); }
    public void genId(){
        int i = 1;
        for(Locker l : lockers){
            l.setLockerId(i);
            i++;
        }
    }

    public void addLocker(Locker locker) {
        boolean duplicate;
        do {
            duplicate = false;
            for (Locker l : lockers) {
                if (l.getLockerUid().equals(locker.getLockerUid())) {
                    // ถ้าเจอซ้ำ สร้างใหม่แล้วเช็คอีกครั้ง
                    locker.setLockerUid(new UuidUtil().generateShort());
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);
        lockers.add(locker);
    }

    public void addLocker(List<Locker> lockers_in) {
        for (Locker locker : lockers_in) {
            boolean duplicate;
            do {
                duplicate = false;
                for (Locker l : lockers) {
                    if (l.getLockerUid().equals(locker.getLockerUid())) {
                        // ถ้าเจอ UID ซ้ำ ให้สร้างใหม่แล้วเช็คอีกครั้ง
                        locker.setLockerUid(new UuidUtil().generateShort());
                        duplicate = true;
                        break;
                    }
                }
            } while (duplicate);
            lockers.add(locker);
        }
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
            if (l.isStatus()) {
                return l;
            }
        }
        return null;
    }

    public Locker findLockerByStatus(boolean status) {
        for (Locker l : lockers) {
            if (l.isStatus()) {
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