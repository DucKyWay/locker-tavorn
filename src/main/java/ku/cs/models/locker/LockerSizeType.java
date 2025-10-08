package ku.cs.models.locker;

public enum LockerSizeType {
    SMALL(1, "เล็ก"),
    MEDIUM(2, "กลาง"),
    LARGE(3, "ใหญ่");

    private final int value;
    private final String description;

    LockerSizeType(int value, String description) {
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
