package ku.cs.models.request;

public enum RequestType {
    SUCCESS(0, "สิ้นสุดการใช้งาน"),
    APPROVE(1, "คำขออนุมัติ"),
    PENDING(2, "คำขอรออนุมัติ"),
    REJECT(3, "คำขอถูกปฏิเสธ"),
    LATE(3, "เลยกำหนด");

    private final int value;
    private final String description;

    RequestType(int value, String description) {
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
