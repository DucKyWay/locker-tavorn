package ku.cs.models;

public class Zone {
    private String zone;
    public Zone() {
    }
    public Zone(String label) {
        this.zone = label;
    }
    public void setZone(String label){
        this.zone = label;
    }
    public String getZone() {
        return zone;
    }
    @Override
    public String toString() {
        return zone;
    }
}
