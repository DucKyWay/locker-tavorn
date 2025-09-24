package ku.cs.models.locker;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
@JsonbPropertyOrder({"uuid", "zone", "status","lockerType","id","role","available"})
public class Locker {

    private String uuid;
    private int id;
    private LockerType lockerType;
    private String zone;
    private boolean available;
    private boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
    public Locker() {
    }
    public Locker(String zone) {
        this.uuid = UuidUtil.generateShort();
        this.zone = zone;
        this.available = true;
        this.status = true;
    }

    public Locker(int id, String zone){
        this( zone);
        this.id = id;
    }

    public Locker(int id, LockerType lockerType, String zone){
        this(zone);
        this.lockerType = lockerType;
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
