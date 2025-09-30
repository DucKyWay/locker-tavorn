package ku.cs.models.zone;

import ku.cs.models.account.Officer;

import java.util.ArrayList;

public class ZoneList {
    private ArrayList<Zone> zones;

    public ZoneList() {
        zones = new ArrayList<>();
    }

    public int genId() {
        if (zones.isEmpty()) return 0;
        return zones.get(zones.size() - 1).getZoneId() + 1; // ปลอดภัยกว่า getLast()
    }

    public void addZone(String label) {
        if (isFindZoneByName(label)) {
            zones.add(new Zone(label, genId()));
        } else {
            System.out.println("Zone already exists");
        }
    }

    public void addZone(Zone zone) {
        if (isFindZoneByName(zone.getZoneName())) {
            zone.setZoneId(genId());
            zones.add(zone);
        } else {
            System.out.println("Zone already exists");
        }
    }

    public void removeZone(Zone zone) {
        zones.remove(zone);
    }

    public void removeZoneByUid(String zoneUid) {
        Zone targetZone = findZoneByUid(zoneUid);
        if (targetZone != null) {
            zones.remove(targetZone);
        }
    }

    public boolean isFindZoneByName(String label) {
        for (Zone zone : zones) {
            if (zone.getZoneName().equals(label)) {
                return false;
            }
        }
        return true;
    }

    public Zone findZoneByUid(String uid) {
        for (Zone zone : zones) {
            System.out.println("checking zoneUid=" + zone.getZoneUid());
            if (zone.getZoneUid().equals(uid)) {
                return zone;
            }
        }
        return null;
    }

    public Zone findZoneByName(String label) {
        for (Zone zone : zones) {
            if (zone.getZoneName().equals(label)) {
                return zone;
            }
        }
        return null;
    }

    public Zone findZoneById(int id) {
        for (Zone zone : zones) {
            if (zone.getZoneId() == id) {
                return zone;
            }
        }
        return null;
    }

    public boolean zoneToggleStatus(Zone zone) {
        if (isFindZoneByName(zone.getZoneName())) {
            zone.toggleStatus();
            return true;
        }
        return false;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }
}
