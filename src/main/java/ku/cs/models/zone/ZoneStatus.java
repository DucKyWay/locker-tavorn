package ku.cs.models.zone;

public enum ZoneStatus {
    ACTIVE(1, "ใช้งานอยู่"),
    INACTIVE(0, "ปิดการใช้งาน"),
    FULL(2, "เต็ม");

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
