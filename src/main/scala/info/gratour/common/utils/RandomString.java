package info.gratour.common.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomString {

    /**
     * Generate a random string.
     * @return random string which characters in symbols
     */
    public String nextString() {
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;
    private final int length;

    /**
     * Create an alphanumeric string generator.
     *
     * @param length the of string returned by {@link RandomString#nextString()}
     * @param random random object
     * @param symbols the string character set
     */
    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.length = length;
    }

    /**
     * Create an alphanumeric string generator.
     * @param length the of string returned by {@link RandomString#nextString()}
     * @param random random object
     */
    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     * @param length the of string returned by {@link RandomString#nextString()}
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomString() {
        this(21);
    }

    public static final Random DEFAULT_RANDOM = new Random();
    public static final String EASY_READ_SYMBOLS = digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";

    public static RandomString easyReadRandomString(int len) {
        return new RandomString(len, DEFAULT_RANDOM, EASY_READ_SYMBOLS);
    }
}
