package ku.cs.models.comparator.Locker;

import ku.cs.models.locker.Locker;

import java.util.Comparator;

public class LockerSizeTypeComparator implements Comparator<Locker> {
    @Override
    public int compare(Locker o1, Locker o2) {
        if(o1.getLockerSizeType().getValue()>o2.getLockerSizeType().getValue())return -1;
        else if(o1.getLockerSizeType().getValue()<o2.getLockerSizeType().getValue())return 1;
        return 0;
    }
}
