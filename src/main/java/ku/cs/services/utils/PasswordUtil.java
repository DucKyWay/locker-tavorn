package ku.cs.services.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    /*
     * SHA-256 ในการแปลงรหัสผ่านให้เป็น one-way hash ก่อนบันทึกลงไฟล์ ซึ่งมีขั้นตอนดังนี้
     * รับรหัสผ่าน
     * แปลงเป็นข้อมูลเป็น byte ด้วย UTF-8
     * นำไปเข้าฟังก์ชัน SHA-256 ได้ค่า hash ความยาว 256 บิต
     * แปลงผลลัพธ์เป็น base(16) แล้วเก็บลงไฟล์แทนรหัสผ่านจริง
     * ตอนเข้าสู่ระบบ จะ hash รหัสผ่านที่กรอก แล้วเปรียบเทียบกับ hash
     */

    private PasswordUtil() {}

    // TODO: ใช้ตอนเริ่ม mock Data
    public static String hashPasswordBCrypt(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(12));
    }

    public static boolean matchesBCrypt(String raw, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(raw, storedHash);
    }

    public static String hashPassword(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(encoded.length * 2);
            for (byte b : encoded) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean matches(String raw, String storedHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String rawHash = hashPassword(raw);
            // same time
            return MessageDigest.isEqual(
                    rawHash.getBytes(StandardCharsets.US_ASCII),
                    storedHash.getBytes(StandardCharsets.US_ASCII)
            );
        } catch (Exception e) {
            return false;
        }
    }
}