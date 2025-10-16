package ku.cs.models.account;

public enum Role {
    ADMIN(1, "ผู้ดูแลระบบ"),
    OFFICER(2,"เจ้าหน้าที่"),
    USER(4, "ผู้ใช้งาน");

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
