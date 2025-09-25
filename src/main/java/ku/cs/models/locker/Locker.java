package ku.cs.models.locker;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.utils.GenerateNumberUtil;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
@JsonbPropertyOrder({"uuid", "zone", "status","lockerType","sizelockerType","id","role","available"})
public class Locker {

    private String uuid;
    private int id;
    private SizeLockerType sizelockerType;
    private LockerType lockerType;
    private String password;
    private String zone;
    private boolean available;
    private boolean status;
    public Locker() {
    }
    public Locker(LockerType lockerType,SizeLockerType sizelockerType, String zone) {
        this.uuid = UuidUtil.generateShort();
        this.lockerType = lockerType;
        this.sizelockerType = sizelockerType;
        if(getLockerType()== LockerType.DIGITAL)this.password = GenerateNumberUtil.generateNumberShort();
        this.zone = zone;
        this.available = true;
        this.status = true;
    }
    public Locker(int id,LockerType lockerType,SizeLockerType sizelockerType, String zone){
        this(lockerType,sizelockerType, zone);
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public SizeLockerType getSizelockerType() {
        return sizelockerType;
    }

    public void setSizelockerType(SizeLockerType sizelockerType) {
        this.sizelockerType = sizelockerType;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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