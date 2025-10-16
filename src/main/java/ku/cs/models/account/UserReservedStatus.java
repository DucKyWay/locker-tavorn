package ku.cs.models.account;

public class UserReservedStatus {
    private String id;
    private String typeKey;
    private String zone;
    private String status;

    UserReservedStatus(String id, String typeKey, String zone, String status) {
        this.id = id;
        this.typeKey = typeKey;
        this.zone = zone;
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public String getTypeKey() {
        return typeKey;
    }
    public String wgetZone() {
        return zone;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserReservedStatus{" +
                "id='" + id + '\'' +
                ", typeKey='" + typeKey + '\'' +
                ", zone='" + zone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
