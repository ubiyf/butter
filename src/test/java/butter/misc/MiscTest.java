package butter.misc;

import butter.support.NumberUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-18
 * Time: 上午10:36
 */
public class MiscTest {
    @Test
    public void testLontToChar() {
        long value = 123345;
        int valueLen = (int) (Math.log(value) / Math.log(10) + 1);
        byte[] chars = new byte[valueLen];
        for (int i = valueLen - 1; i >= 0; i--) {
            long digit = value % 10;
            chars[i] = (byte) ('0' + digit);
            value = value / 10;
        }

        String charStr = new String(chars);

        Assert.assertEquals("123345", charStr);
    }

    @Test
    public void testLongToCharByteBuf() {
        ByteBuf buf = Unpooled.buffer();
        longToCharByteBuf(buf, 12334445);

        byte[] chars = new byte[buf.readableBytes()];
        buf.readBytes(chars);
        String charStr = new String(chars);

        Assert.assertEquals("12334445", charStr);
    }

    private void longToCharByteBuf(ByteBuf buf, long value) {
        buf.clear();
        int valueLen = getLongLength3((int) value);
        buf.writeZero(valueLen);
        int writeIdx = buf.writerIndex();
        for (int i = 0; i < valueLen; i++) {
            long digit = value % 10;
            buf.setByte(writeIdx - i - 1, (byte) ('0' + digit));
            value = value / 10;
        }
    }

    private int getLongLength(long value) {
        return (int) (Math.log(value) / Math.log(10) + 1);
    }

    private int getLongLength2(long value) {
        int length = 0;
        while (value > 0) {
            length++;
            value = value / 10;
        }
        return length;
    }

    private static final int MAX_NUMBER_LEN = 10;
    private static final int MIN_NUMBER_LEN = 0;
    private static final int[] NUMS = {0, 0xa, 0x64, 0x3E8, 0x2710, 0x186A0, 0xF4240, 0x989680, 0x5F5E100, 0x3B9ACA00, 0x3B9ACA01};

    private int getLongLength3(long value) {
        if (value >= NUMS[MAX_NUMBER_LEN]) {
            return MAX_NUMBER_LEN;
        }

        int l = MIN_NUMBER_LEN;
        int r = MAX_NUMBER_LEN;
        while (l <= r) {
            int mid = (l + r) >> 1;
            if (NUMS[mid] == value) return mid + 1;
            else if (NUMS[mid] < value) l = mid + 1;
            else r = mid - 1;
        }
        return r + 1;
    }

    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    // Requires positive x
    static int stringSize(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    @Test
    public void testLongToCharByteBufPerformance() {
        ByteBuf buf = Unpooled.buffer();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            longToCharByteBuf(buf, 12);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        Assert.assertTrue((end - start) < 150);
    }

    @Test
    public void testTooLongStringParseToInt() {
//        Integer.parseInt("9999999999999");
        System.out.println(-Integer.MIN_VALUE * 10);
        System.out.println(-Integer.MAX_VALUE * 10);
    }

    @Test
    public void testIntegerToBytes() {
        byte[] expect1 = "12321231123".getBytes();
        byte[] expect2 = "-12321231123".getBytes();
        byte[] data1 = NumberUtils.integerToBytes(12321231123L);
        byte[] data2 = NumberUtils.integerToBytes(-12321231123L);
        for (int i = 0; i < data1.length; i++) {
            Assert.assertEquals(expect1[i], data1[i]);
        }
        for (int i = 0; i < data2.length; i++) {
            Assert.assertEquals(expect2[i], data2[i]);
        }
    }

    @Test
    public void testFloatToBytes() throws Exception {
        double d = 3.141592654;
        String result = "3.141592654";
        Assert.assertEquals(result, Double.toString(d));
    }
}
