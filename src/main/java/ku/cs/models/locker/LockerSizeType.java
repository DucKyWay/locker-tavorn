package ku.cs.models.locker;

public enum LockerSizeType {
    SMALL(1, "เล็ก",10,20),
    MEDIUM(2, "กลาง",20,40),
    LARGE(3, "ใหญ่",30,60);

    private final int value;
    private final String description;
    private final int price;
    private final int fine;

    LockerSizeType(int value, String description, int price, int fine) {
        this.value = value;
        this.description = description;
        this.price = price;
        this.fine = fine;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }
    public int getFine() {
        return fine;
    }
}

