package ku.cs.services.utils;

import java.util.Random;

public final class GenerateNumberUtil {

    private final Random random = new Random();

    /**
     * Generate Number 5 digits
     * @return random number of 00000-99999
     */
    public String generateNumberShort() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
