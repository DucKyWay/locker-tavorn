package ku.cs.models.locker;

import ku.cs.services.utils.UuidUtil;

import java.sql.Struct;

public class LockerDigital extends Locker{
    LockerType lockerType;
    String password;

    public LockerDigital(String zone) {
        this(zone, UuidUtil.generateShort());
    }

    public LockerDigital(String zone, String password) {
        super(zone);
        this.lockerType = LockerType.DIGITAL;
        this.password = password;
    }

    public LockerDigital(int id, String zone) {
        this(id, zone, UuidUtil.generateShort());
    }

    public LockerDigital(int id, String zone, String password) {
        super(id, zone);
        this.lockerType = LockerType.DIGITAL;
        this.password = password;
    }

    public LockerType getLockerType() {
        return lockerType;
    }
}
