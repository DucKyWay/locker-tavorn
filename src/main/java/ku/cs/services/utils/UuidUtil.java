package ku.cs.services.utils;

import java.util.UUID;

public final class UuidUtil {
    public UuidUtil() {}

    public String generate() {
        return UUID.randomUUID().toString();
    }

    // ไม่มี dash (-)
    public String generateCompact() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 5 ตัว
    public String generateShort() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
