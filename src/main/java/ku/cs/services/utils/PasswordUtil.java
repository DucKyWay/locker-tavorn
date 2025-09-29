package ku.cs.services.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    /*
     * เข้ารหัสด้วย BCrypt
     */

    private PasswordUtil() {}

    public static String hashPassword(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(12));
    }

    public static boolean matches(String raw, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(raw, storedHash);
    }
}