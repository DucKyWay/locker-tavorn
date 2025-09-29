package ku.cs.models.comparator;

import ku.cs.models.account.Officer;
import ku.cs.models.zone.Zone;

import java.util.Comparator;

public class OfficerZoneComparator implements Comparator<Zone> {
    private final Officer officer;

    public OfficerZoneComparator(Officer officer) {
        this.officer = officer;
    }

    @Override
    public int compare(Zone z1, Zone z2) {
        boolean z1In = officer.getZoneUids().contains(z1.getZoneUid());
        boolean z2In = officer.getZoneUids().contains(z2.getZoneUid());

        if (z1In && !z2In) return -1; // z1 ก่อน
        if (!z1In && z2In) return 1;  // z2 ก่อน

        return z1.getZoneUid().compareTo(z2.getZoneUid());
    }
}
