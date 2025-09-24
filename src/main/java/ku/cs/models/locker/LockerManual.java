package ku.cs.models.locker;

public class LockerManual extends Locker{
    LockerType lockerType;

    public LockerManual(String zone) {
        super(zone);
        this.lockerType = LockerType.DIGITAL;
    }

    public LockerManual(int id,String zone) {
        super(id, zone);
        this.lockerType = LockerType.MANUAL;
    }

    public LockerType getLockerType() {
        return lockerType;
    }
}
