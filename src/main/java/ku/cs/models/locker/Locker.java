package ku.cs.models.locker;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.utils.GenerateNumberUtil;
import ku.cs.services.utils.UuidUtil;

@JsonbPropertyOrder({"lockerUid", "id", "zoneUid", "zoneName", "status", "lockerType", "lockerSizeType", "role", "available"})
public class Locker {
    private String lockerUid;
    private int id;
    private LockerSizeType lockerSizeType;
    private LockerType lockerType;
    private String password;
    private String zoneUid;
    private String zoneName;
    private boolean available;
    private boolean status;
    public Locker() {

    }
    public Locker(LockerType lockerType, LockerSizeType lockerSizeType, String zoneName) {
        this.lockerUid = new UuidUtil().generateShort();
        this.lockerType = lockerType;
        this.lockerSizeType = lockerSizeType;
        if(getLockerType()== LockerType.DIGITAL)this.password = GenerateNumberUtil.generateNumberShort();
        this.zoneName = zoneName;
        this.available = true;
        this.status = true;
    }
    public Locker(int id, LockerType lockerType, LockerSizeType lockerSizeType, String zoneName){
        this(lockerType, lockerSizeType, zoneName);
        this.id = id;
    }

    public String getUid() {
        return lockerUid;
    }

    public void setUid(String lockerUid) {
        this.lockerUid = lockerUid;
    }
    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public LockerSizeType getLockerSizeType() {
        return lockerSizeType;
    }

    public String getLockerSizeTypeString() {
        return lockerSizeType.getDescription();
    }

    public void setLockerSizeType(LockerSizeType lockerSizeType) {
        this.lockerSizeType = lockerSizeType;
    }

    public LockerType getLockerType() {
        return lockerType;
    }

    public String getLockerTypeString() {
        return lockerType.getDescription();
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

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
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