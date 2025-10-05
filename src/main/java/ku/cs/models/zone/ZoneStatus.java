package ku.cs.models.zone;

public enum ZoneStatus {
    INACTIVE(0, "ปิดการใช้งาน"),
    ACTIVE(1, "เปิดการใช้งาน"),
    FULL(2, "ผู้ใช้งานเต็ม");

    private final int value;
    private final String description;

    ZoneStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
