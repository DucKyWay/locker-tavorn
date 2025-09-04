package ku.cs.models.zone;

public class Zone {
    private String zone;
    private int idZone;
    public Zone() {
    }
    public Zone(String label, int idZone) {
        this.zone = label;
        this.idZone = idZone;
    }
    public void setZone(String label){
        this.zone = label;
    }
    public String getZone() {
        return zone;
    }
    public int getIdZone() {
        return idZone;
    }
    @Override
    public String toString() {
        return zone;
    }

    public void setIdZone(int i) {
        this.idZone = i;
    }
}
