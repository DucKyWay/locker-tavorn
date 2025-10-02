package ku.cs.services.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    /*
     * เข้ารหัสด้วย BCrypt
     */

    public PasswordUtil() {}

    public String hashPassword(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(12));
    }

    public boolean matches(String raw, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(raw, storedHash);
    }
}