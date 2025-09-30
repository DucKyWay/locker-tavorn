package ku.cs.models.request;

public enum RequestType {
    REJECT(0, "คำขอถูกปฏิเสธ"),
    APPROVE(1, "คำขออนุมัติ"),
    SUCCESS(2, "สำเร็จ"),
    PENDING(3, "คำขอรออนุมัติ"),
    LATE(4, "เลยกำหนด");

    private final int code;
    private final String description;

    RequestType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
