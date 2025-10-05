package ku.cs.models.account;

public enum Role {
    ADMIN(1, "Admin"),
    OFFICER(2,"Officer"),
    USER(4, "User");

    private final int value;
    private final String description;

    Role(int value, String description) {
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
