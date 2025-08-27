package ku.cs.models;

import java.util.ArrayList;

public class ZoneList {
    private ArrayList<Zone> zones;
    public ZoneList(){
        zones = new ArrayList<>();
    }
    public void addZone(String label){
        if(!fineZone(label)){
            zones.add(new Zone(label));
        }else{
            System.out.println("Zone already exists");
        }
    }
    public void addZone(Zone zone){
        if(!fineZone(zone.getZone())){
            zones.add(zone);
        }else{
            System.out.println("Zone already exists");
        }
    }
    public void removeZone(String label){
        if(containsZone(label)){
            zones.remove(new Zone(label));
        }else{
            System.out.println("Zone does not exist");
        }

    }
    public boolean fineZone(String label){
        for(Zone zone: zones){
            if(zone.getZone().equals(label)){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Zone> getZones(){
        return zones;
    }
    public boolean containsZone(String label){
        return zones.contains(new Zone(label));
    }
}
