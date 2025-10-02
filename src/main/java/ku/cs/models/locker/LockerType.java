package ku.cs.models.locker;

public enum LockerType {
    MANUAL(0, "Manual"),
    DIGITAL(1, "Digital");

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
