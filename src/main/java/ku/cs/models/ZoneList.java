package ku.cs.models;

import java.util.ArrayList;

public class ZoneList {
    private ArrayList<Zone> zones;
    public ZoneList(){
        zones = new ArrayList<>();
    }
    public void addZone(String label){
        if(containsZone(label)){
            zones.add(new Zone(label));
        }else{
            System.out.println("Zone already exists");
        }
    }
    public void addZone(Zone zone){
        zones.add(zone);
    }
    public void removeZone(String label){
        if(containsZone(label)){
            zones.remove(new Zone(label));
        }else{
            System.out.println("Zone does not exist");
        }

    }
    public ArrayList<Zone> getZones(){
        return zones;
    }
    public boolean containsZone(String label){
        return zones.contains(new Zone(label));
    }
}
