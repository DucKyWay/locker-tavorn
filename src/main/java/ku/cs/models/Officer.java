package ku.cs.models;

public class Officer extends Account {

    private String serviceZone;

    public Officer() { super(); }
    public Officer(int idZone,String username, String name, String password,
                   String email, String telphone, String imagePath) {
        super(createUsername(idZone,username), name, password, email, telphone, imagePath);
    }
    public Officer(int idZone,String username, String name, String password,
                   String email, String telphone, int requestCount, String imagePath) {
        super(createUsername(idZone,username), name, password, email, telphone, imagePath);
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
    public String getRole() { return "OFFICER"; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", serviceZone='" + serviceZone + "'}");
    }
}
