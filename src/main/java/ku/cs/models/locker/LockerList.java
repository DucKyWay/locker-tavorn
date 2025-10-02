package ku.cs.models.locker;

import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;

public class LockerList {
    private ArrayList<Locker> lockers;
    public LockerList() { lockers = new ArrayList<>(); }
    public void genId(){
        int i = 0;
        for(Locker l : lockers){
            l.setId(i);
            i++;
        }
    }

    public void addLocker(Locker locker) {
        boolean duplicate;
        do {
            duplicate = false;
            for (Locker l : lockers) {
                if (l.getUid().equals(locker.getUid())) {
                    // ถ้าเจอซ้ำ สร้างใหม่แล้วเช็คอีกครั้ง
                    locker.setUid(new UuidUtil().generateShort());
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);
        lockers.add(locker);
    }

    public void deleteLocker(Locker locker) {
        lockers.remove(locker);
    }

    public Locker findLockerByUuid(String uuid) {
        for (Locker l : lockers) {
            if (l.getUid().equals(uuid)) {
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
    public String getZone(){
        if(lockers.size() == 0){
            return null;
        }
        else{
            return lockers.get(0).getZoneName();
        }

    }
    public ArrayList<Locker> getLockers() {
        return lockers;
    }
}