package ku.cs.models.locker;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.utils.GenerateNumberUtil;
import ku.cs.services.utils.UuidUtil;

@JsonbPropertyOrder({"lockerUid", "id", "zoneUid", "status", "lockerType", "lockerSizeType", "role", "available","imagePath"})
public class Locker {
    private String lockerUid;
    private int lockerId;
    private LockerSizeType lockerSizeType;
    private LockerType lockerType;
    private String password;
    private String zoneUid;
    private String imagePath;
    private boolean available;
    private boolean status;

    public Locker(LockerType lockerType, LockerSizeType lockerSizeType, String zoneUid, String imagePath) {
        this.lockerUid = new UuidUtil().generateShort();
        this.lockerType = lockerType;
        this.lockerSizeType = lockerSizeType;
        if(getLockerType()== LockerType.DIGITAL)
            this.password = new GenerateNumberUtil().generateNumberShort();
        this.zoneUid = zoneUid;
        this.available = true;
        this.status = true;
        this.imagePath = imagePath;
    }
    public Locker(int lockerId, LockerType lockerType, LockerSizeType lockerSizeType, String zoneUid){
        this(lockerType, lockerSizeType, zoneUid,"");
        this.lockerId = lockerId;
    }


    public String getLockerUid() {
        return lockerUid;
    }
    public void setLockerUid(String lockerUid) {
        this.lockerUid = lockerUid;
    }

    public int getLockerId() {
        return lockerId;
    }

    public void setLockerType(String type) {
        if(type != null) {
            try {
                this.lockerType = LockerType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                this.lockerType = null; // หรือค่า default เช่น MANUAL
            }
        }
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setLockerId(int lockerId) {
        this.lockerId = lockerId;
    }

    public LockerSizeType getLockerSizeType() {
        return lockerSizeType;
    }

    public void setLockerSizeType(LockerSizeType lockerSizeType) {
        this.lockerSizeType = lockerSizeType;
    }

    public LockerType getLockerType() {
        return lockerType;
    }

    public void setLockerType(LockerType lockerType) {
        this.lockerType = lockerType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZoneUid() {
        return zoneUid;
    }

    public void setZoneUid(String zoneUid) {
        this.zoneUid = zoneUid;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}