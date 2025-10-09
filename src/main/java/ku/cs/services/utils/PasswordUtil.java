package ku.cs.services.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    /*
     * เข้ารหัสด้วย BCrypt
     */

    public PasswordUtil() {}

    /**
     * Hash password by BCrypt
     * @param raw Raw Password
     * @return hashed password by BCrypt
     */
    public String hashPassword(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(12));
    }

    /**
     * Check matches password by BCrypt
     * @param raw Raw Password to check
     * @param storedHash Hashed password has stored
     * @return {{@code true} password matches, {@code false} password doesn't match.}
     */
    public boolean matches(String raw, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(raw, storedHash);
    }
}