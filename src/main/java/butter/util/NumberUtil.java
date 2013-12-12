package butter.util;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-12
 * Time: 下午2:21
 */
public class NumberUtil {

    public static int stringSize(long x) {
        if (x == 0) {
            return 1;
        }

        if (x < 0) {
            return stringSize(-x) + 1;
        }

        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p)
                return i;
            p = 10 * p;
        }
        return 19;
    }

    public static long bytesToInteger(byte[] b) {
        if (b == null) {
            throw new NumberFormatException("null");
        }

        if (b.length == 0) {
            throw new NumberFormatException("zero length data");
        }

        int radix = 10;
        long result = 0;
        boolean negative = false;
        int i = 0, len = b.length;
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = (char) b[0];
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+') {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }

                if (len == 1) {// Cannot have lone "+" or "-"
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit((char) b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                if (result < multmin) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                result -= digit;
            }
        } else {
            throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
        }
        return negative ? result : -result;
    }

    private final static byte[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static byte[] integerToBytes(long i) {
        if (i == Long.MIN_VALUE)
            return "-9223372036854775808".getBytes();
        boolean negative = false;
        if (i < 0) {
            negative = true;
            i = -i;
        }

        int size = negative ? stringSize(i) + 1 : stringSize(i);
        byte[] bytes = new byte[size];
        int charPos = size - 1;
        do {
            if (negative && charPos == 0) {
                bytes[charPos--] = '-';
            } else {
                bytes[charPos--] = digits[(int) (i % 10)];
            }
            i = i / 10;
        } while (charPos > -1);

        return bytes;
    }

}
