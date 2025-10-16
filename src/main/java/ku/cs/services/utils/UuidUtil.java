package ku.cs.services.utils;

import java.util.UUID;

public final class UuidUtil {
    public UuidUtil() {}

    public String generateShort() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
