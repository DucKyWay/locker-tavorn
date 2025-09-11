package ku.cs.models.account;

import java.time.LocalDateTime;

public class Officer extends Account {

    private String serviceZone;
    private int requestCount=0;

    public Officer() { super(); }

    public Officer(String username, String name, String password,
                   String email, String telphone, Role role, LocalDateTime logintime) {
        super(username, name, password, email, telphone, role,logintime);
    }

    public Officer(String username, String name, String password,
                   String email, String telphone, int requestCount, Role role,LocalDateTime logintime) {
        this(username, name, password, email, telphone, role, logintime);
        this.requestCount = requestCount;
    }

    public Officer(int zone, String username, String name, String password,
                   String email, String telphone, Role role, LocalDateTime logintime) {
        this(createUsername(zone, username), name, password, email, telphone, role, logintime);
    }

    public Officer(int zone, String username, String name, String password,
                   String email, String telphone, int requestCount, Role role,LocalDateTime logintime) {
        this(createUsername(zone, username), name, password, email, telphone,requestCount, role, logintime);
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
