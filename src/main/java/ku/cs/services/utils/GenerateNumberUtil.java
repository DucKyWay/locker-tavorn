package ku.cs.services.utils;

import java.util.Random;

public class GenerateNumberUtil {
    private static final Random random = new Random();
    public static String generateNumberShort() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
