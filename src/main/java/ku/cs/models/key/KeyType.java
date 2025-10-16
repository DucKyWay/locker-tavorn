package ku.cs.models.key;

public enum KeyType {
    MANUAL(0, "แม่กุญแจ"),
    CHAIN(0, "สายล็อครหัส"),
    DIGITAL(4, "ดิจิทัล"),
    ;

    private final int value;
    private final String description;
    KeyType(int value, String description) {
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
