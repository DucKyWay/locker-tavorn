package ku.cs.services;

import ku.cs.models.LockerList;
import ku.cs.models.LockerType;

public class LockerListHardCodeDatasource {
    public static LockerList readdata() {
        LockerList lockers = new LockerList();
        lockers.addLocker(LockerType.CHAIN, "Phahonyothin");
        lockers.addLocker(LockerType.DIGITAL, "Ladprao");
        lockers.addLocker(LockerType.DIGITAL, "Ladprao");
        lockers.addLocker(LockerType.MANUAL, "Ladprao");
        lockers.addLocker(LockerType.MANUAL, "Phahonyothin");

        return lockers;
    }
}
