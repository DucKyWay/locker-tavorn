package ku.cs.services;

import ku.cs.models.LockerList;
import ku.cs.models.KeyType;

public class LockerListHardCodeDatasource {
    public static LockerList readdata() {
        LockerList lockers = new LockerList();
        lockers.addLocker(KeyType.CHAIN, "Phahonyothin");
        lockers.addLocker(KeyType.DIGITAL, "Ladprao");
        lockers.addLocker(KeyType.DIGITAL, "Ladprao");
        lockers.addLocker(KeyType.MANUAL, "Ladprao");
        lockers.addLocker(KeyType.MANUAL, "Phahonyothin");

        return lockers;
    }
}
