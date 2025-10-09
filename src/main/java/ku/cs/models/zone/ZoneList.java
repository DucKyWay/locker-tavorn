package ku.cs.models.zone;

import java.util.ArrayList;
import java.util.List;

public class ZoneList {
    private final List<Zone> zones;

    public ZoneList() {
        zones = new ArrayList<>();
    }

    /*
     * Generate Zone Auto Increment Identifier
     */
    public int genId() {
        if (zones.isEmpty()) return 0;
        return zones.get(zones.size()-1).getZoneId() + 1; // ปลอดภัยกว่า getLast()
    }

    /**
     * Add Zone to list By Name
     *
     * @param label Zone name
     */
    public boolean addZone(String label) {
        if (hasZoneByName(label)) {
            System.out.println("Zone already exists");
            return false;
        }
        zones.add(new Zone(label, genId()));
        return true;
    }

    /**
     * Add Zone to list by Zone
     *
     * @param zone Zone Model
     */
    public boolean addZone(Zone zone) {
        if (hasZoneByName(zone.getZoneName())) {
            System.out.println("Zone already exists");
            return false;
        }
        zone.setZoneId(genId());
        zones.add(zone);
        return true;
    }

    /**
     * Remove Zone in list by Zone
     *
     * @param zone Zone model
     */
    public void removeZone(Zone zone) {
        zones.remove(zone);
    }

    /**
     * Remove Zone in list by Zone Unique Identifier
     *
     * @param zoneUid Zone Unique Identifier
     */
    public void removeZoneByUid(String zoneUid) {
        Zone targetZone = findZoneByUid(zoneUid);
        if (targetZone != null) {
            zones.remove(targetZone);
        }
    }

    /**
     * Check has zone on list
     *
     * @param zoneName Zone Name
     * @return {@code true} if found zone on ZoneList, {@code false} another case.
     */
    public boolean hasZoneByName(String zoneName) {
        return zones.stream().anyMatch(z -> z.getZoneName().equalsIgnoreCase(zoneName));
    }

    /* ====================================================================
     *  Find Methods
     *  ==================================================================== */

    /**
     * Search Zone by Unique Identifier
     * @param zoneUid Zone Unique Identifier to search
     * @return Zone Model that has zoneUid
     */
    public Zone findZoneByUid(String zoneUid) {
        for (Zone zone : zones) {
            if (zone.getZoneUid().equals(zoneUid)) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Search Zone by Name
     * @param zoneName Zone Name to search
     * @return Zone Model that has zoneName
     */
    public Zone findZoneByName(String zoneName) {
        for (Zone zone : zones) {
            if (zone.getZoneName().equals(zoneName)) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Search Zone by Identifier
     * @param zoneId Zone Identifier to search
     * @return Zone Model that has zoneId
     */
    public Zone findZoneById(int zoneId) {
        for (Zone zone : zones) {
            if (zone.getZoneId() == zoneId) {
                return zone;
            }
        }
        return null;
    }

    /* ====================================================================
     *  Getter
     *  ==================================================================== */

    /**
     * Get Zone List on current stored.
     * @return list of zone objects
     */
    public List<Zone> getZones() {
        return zones;
    }
}
