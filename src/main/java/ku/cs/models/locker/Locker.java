package ku.cs.models.locker;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
@JsonbPropertyOrder({"uuid", "zone", "status","lockerType", "keyType","id","role","available"})
public class Locker {

    private final String uuid;
    private int id;
    private KeyType keyType;
    private String zone;
    private boolean available;
    private boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
    public Locker() {
        this.uuid = UuidUtil.generateShort();
    }
    public Locker(KeyType type, String zone) {
        this.uuid = UuidUtil.generateShort();
        this.keyType = type;
        this.zone = zone;
        this.available = true;
        this.status = true;
    }
    public Locker(int id,KeyType type, String zone){
        this(type, zone);
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public String getLockerType() {
        return (keyType == KeyType.MANUAL || keyType == KeyType.CHAIN)
                ? "MANUAL"
                : "DIGITAL";
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public String getZone() {
        return zone;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void toggleAvailable() {
        this.available = !this.available;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void toggleStatus() {
        this.status = !this.status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setId(int id) {
        this.id = id;
    }

    // today
    public void setStartDate() {
        this.startDate = LocalDate.now();
    }

    // set day
    public void setStartDate(LocalDate startDay) {
        this.startDate = startDay;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // today
    public void setEndDate() {
        this.endDate = LocalDate.now();
    }

    // set day
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
