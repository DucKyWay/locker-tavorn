package ku.cs.models;

public class Officer extends Account {

    private String serviceZone;

    public Officer() { super(); }

    public Officer(String username, String name, String password,
                   String email, String telphone, String imagePath) {
        super(username, name, password, email, telphone, imagePath);
    }

    public Officer(String username, String name, String password,
                   String email, String telphone, int requestCount, String imagePath) {
        super(username, name, password, email, telphone, imagePath);
    }

    public String getServiceZone() { return serviceZone; }
    public void setServiceZone(String serviceZone) { this.serviceZone = serviceZone; }

    public boolean isInServiceZone(String zone) {
        return serviceZone != null && zone != null && serviceZone.equalsIgnoreCase(zone);
    }

    public boolean canServe(String zone) {
        return isInServiceZone(zone);
    }

    @Override
    public String getRole() { return "OFFICER"; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", serviceZone='" + serviceZone + "'}");
    }
}
