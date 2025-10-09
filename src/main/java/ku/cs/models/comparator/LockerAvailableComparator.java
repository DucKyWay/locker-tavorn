package ku.cs.models.comparator;

import ku.cs.models.locker.Locker;

import java.util.Comparator;

public class LockerAvailableComparator implements Comparator<Locker> {
    @Override
    public int compare(Locker o1, Locker o2) {
        if(o1.isStatus()&& o2.isStatus()) {
        if (o1.isAvailable() && !o2.isAvailable()) return -1;
            else if (!o1.isAvailable() && o2.isAvailable()) return 1;
            else return 0;
        }
        else if(o1.isStatus() && !o2.isStatus()) {
            return -1;
        }
        else if(!o1.isStatus() && o2.isStatus()) {
            return 1;
        }
        return 0;
    }
}
