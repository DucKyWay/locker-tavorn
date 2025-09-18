package ku.cs.models.account;

public class Officer extends Account {

    private String serviceZone;

    public Officer() { super(); }

    public Officer(String username, String name, String password,
                   String email, String telphone, Role role) {
        super(username, name, password, email, telphone, role);
    }

    public Officer(String username, String name, String password, int zoneId, String email, String phone) {
        super(createUsername(zoneId, username), name, password, email, phone, Role.OFFICER);
    }

    public Officer(int zone, String username, String name, String password,
                   String email, String telphone, Role role) {
        super(createUsername(zone, username), name, password, email, telphone, role);
    }

    public Officer(int zone, String username, String name, String password,
                   String email, String telphone, int requestCount, Role role) {
        super(createUsername(zone, username), name, password, email, telphone, role);
    }
    public static String createUsername(int zone,String username){
        String result = 'z'+ Integer.toString(zone);
        result +="-"+username;
        return result;
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
    public String toString() {
        return super.toString().replace("}", ", serviceZone='" + serviceZone + "'}");
    }
}
