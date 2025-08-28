package ku.cs.services;

import java.util.UUID;

public final class UuidUtil {
    private UuidUtil() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    // ไม่มี dash (-)
    public static String generateCompact() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 5 ตัว
    public static String generateShort() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
