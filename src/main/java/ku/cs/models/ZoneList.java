package ku.cs.models;

import java.util.ArrayList;

public class ZoneList {
    private ArrayList<Zone> zones;

    public ZoneList() {
        zones = new ArrayList<>();
    }

    public int genId() {
        if (zones.isEmpty()) return 0;
        return zones.get(zones.size() - 1).getIdZone() + 1; // ปลอดภัยกว่า getLast()
    }

    public void addZone(String label) {
        if (isFindZoneByName(label)) {
            zones.add(new Zone(label, genId()));
        } else {
            System.out.println("Zone already exists");
        }
    }

    public void addZone(Zone zone) {
        if (isFindZoneByName(zone.getZone())) {
            zone.setIdZone(genId());
            zones.add(zone);
        } else {
            System.out.println("Zone already exists");
        }
    }

    public void removeZoneByName(String label) {
        Zone target = findZoneByName(label);
        if (target != null) {
            zones.remove(target);
        } else {
            System.out.println("Zone does not exist");
        }
    }

    public void removeZoneById(int idZone) {
        Zone target = findZoneById(idZone);
        if (target != null) {
            zones.remove(target);
        }
    }

    public boolean isFindZoneByName(String label) {
        for (Zone zone : zones) {
            if (zone.getZone().equals(label)) {
                return false;
            }
        }
        return true;
    }

    public boolean isFindZoneById(int id) {
        for (Zone zone : zones) {
            if (zone.getIdZone() == id) {
                return true;
            }
        }
        return false;
    }

    public Zone findZoneByName(String label) {
        for (Zone zone : zones) {
            if (zone.getZone().equals(label)) {
                return zone;
            }
        }
        return null;
    }

    public Zone findZoneById(int id) {
        for (Zone zone : zones) {
            if (zone.getIdZone() == id) {
                return zone;
            }
        }
        return null;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }
}
