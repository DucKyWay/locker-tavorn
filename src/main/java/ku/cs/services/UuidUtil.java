package ku.cs.services;

import java.util.UUID;

public final class UuidUtil {
    UUID uuid;

    private UuidUtil() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    // ไม่มี dash (-)
    public static String generateCompact() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
