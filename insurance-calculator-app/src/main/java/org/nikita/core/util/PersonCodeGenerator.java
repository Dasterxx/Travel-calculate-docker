package org.nikita.core.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.function.Predicate;

@Component
public class PersonCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a code in format: Letter - 5 digits (e.g. A-12345)
     * Combines current timestamp seconds + random suffix to reduce collisions.
     */
    public static String generate() {
        char letter = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));

        // Use current epoch seconds mod 100000 to get 5 digits
        int timestampPart = (int) (Instant.now().getEpochSecond() % 100_000);

        // Add a random 2-digit suffix to reduce collision further
        int randomSuffix = RANDOM.nextInt(100);

        // Compose final number part: timestampPart * 100 + randomSuffix (max 7 digits, but we format last 5 digits)
        int numberPart = (timestampPart * 100 + randomSuffix) % 100_000;

        return String.format("%c-%05d", letter, numberPart);
    }

    /**
     * Generates a unique code by retrying until the uniqueness predicate returns true.
     * @param isUniquePredicate a function that returns true if the code is unique (e.g. DB check)
     * @param maxAttempts maximum attempts before throwing exception
     * @return unique person code string
     */
    public static String generateUnique(Predicate<String> isUniquePredicate, int maxAttempts) {
        for (int i = 0; i < maxAttempts; i++) {
            String code = generate();
            if (isUniquePredicate.test(code)) {
                return code;
            }
        }
        throw new IllegalStateException("Failed to generate unique person code after " + maxAttempts + " attempts");
    }
}
