package ku.cs.models.locker;

public enum LockerType {
    MANUAL(2, "แม่กุญแจ"),
    DIGITAL(4, "รหัสผ่าน");

    private final int value;
    private final String description;

    LockerType(int value, String description) {
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
