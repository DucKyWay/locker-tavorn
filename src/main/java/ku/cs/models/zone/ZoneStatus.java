package ku.cs.models.zone;

public enum ZoneStatus {
    ACTIVE(1, "ใช้งานอยู่"),
    INACTIVE(0, "ปิดการใช้งาน"),
    FULL(2, "เต็ม");

    private final int code;
    private final String description;

    ZoneStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
