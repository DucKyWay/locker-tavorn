package ku.cs.models.comparator.Locker;

import ku.cs.models.locker.Locker;

import java.util.Comparator;

public class LockerIdComparator implements Comparator<Locker> {
    @Override
    public int compare(Locker o1, Locker o2) {
        if(o1.getLockerId() > o2.getLockerId())return 1;
        else if(o1.getLockerId() < o2.getLockerId())return -1;
        else return 0;
    }
}
