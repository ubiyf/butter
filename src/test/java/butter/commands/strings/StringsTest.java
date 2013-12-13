package butter.commands.strings;

import butter.commands.RedisTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-13
 * Time: 下午2:15
 */
public class StringsTest extends RedisTest {
    @Test
    public void testAppend() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = " World".getBytes();
        final byte[] result = "Hello World".getBytes();
        assertEquals(5, conn.append(myKey, hello));
        assertEquals(11, conn.append(myKey, world));
        bytesEqual(result, conn.get(myKey));
    }

    @Test
    public void testBitCount() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] value = "foobar".getBytes();

        conn.set(myKey, value);
        assertEquals(26, conn.bitCount(myKey));
        assertEquals(4, conn.bitCount(myKey, 0, 0));
        assertEquals(6, conn.bitCount(myKey, 1, 1));
    }
}
