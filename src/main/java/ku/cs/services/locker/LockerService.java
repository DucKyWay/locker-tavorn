package ku.cs.services.locker;

import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerSizeType;
import ku.cs.models.locker.LockerType;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;

public class LockerService {

    private final LockerList lockerList = new LockerDatasourceProvider().loadAllCollections();
    public Locker createLocker(LockerType selectedType, LockerSizeType selectedSize, String zoneUid) {
        Locker newLocker;

        do {
            newLocker = new Locker(selectedType, selectedSize, zoneUid, "");
        } while (isDuplicateUid(newLocker.getLockerUid()));

        return newLocker;
    }

    private boolean isDuplicateUid(String lockerUid) {
        return lockerList.getLockers().stream()
                .anyMatch(locker -> locker.getLockerUid().equals(lockerUid));
    }
}
