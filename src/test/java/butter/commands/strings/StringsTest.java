package butter.commands.strings;

import butter.commands.RedisTest;
import butter.exception.CommandInterruptedException;
import butter.protocol.BitOPs;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

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

    @Test
    public void testBitOP() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] dest = "dest".getBytes();
        final byte[] result = "`bc`ab".getBytes();
        final byte[] value1 = "foobar".getBytes();
        final byte[] value2 = "abcdef".getBytes();

        conn.set(key1, value1);
        conn.set(key2, value2);
        conn.bitOP(BitOPs.AND, dest, key1, key2);

        bytesEqual(result, conn.get(dest));
    }

    @Test
    public void testDecr() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] value = "10".getBytes();

        conn.set(myKey, value);
        long result = conn.decr(myKey);
        assertEquals(9, result);

        conn.set(myKey, "234293482390480948029348230948".getBytes());
        expectedEx.expect(CommandInterruptedException.class);
        conn.decr(myKey);
    }

    @Test
    public void testDecrBy() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] value = "10".getBytes();
        conn.set(myKey, value);
        assertEquals(5, conn.decrBy(myKey, 5));
    }

    @Test
    public void testGet() throws Exception {
        final byte[] nonExisting = "nonExisting".getBytes();
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();

        assertNull(conn.get(nonExisting));
        conn.set(myKey, hello);
        bytesEqual(hello, conn.get(myKey));
    }

    @Test
    public void testGetBit() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        assertEquals(0, conn.setBit(myKey, 7, 1));
        assertEquals(0, conn.getBit(myKey, 0));
        assertEquals(1, conn.getBit(myKey, 7));
        assertEquals(0, conn.getBit(myKey, 100));
    }

    @Test
    public void testGetRange() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] value = "This is a string".getBytes();
        conn.set(myKey, value);
        bytesEqual("This".getBytes(), conn.getRange(myKey, 0, 3));
        bytesEqual("ing".getBytes(), conn.getRange(myKey, -3, -1));
        bytesEqual(value, conn.getRange(myKey, 0, -1));
        bytesEqual("string".getBytes(), conn.getRange(myKey, 10, 100));
    }

    @Test
    public void testGetSet() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();

        conn.set(myKey, hello);
        bytesEqual(hello, conn.getSet(myKey, world));
        bytesEqual(world, conn.get(myKey));
    }

    @Test
    public void testIncr() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        conn.set(myKey, "10".getBytes());
        conn.incr(myKey);
        bytesEqual("11".getBytes(), conn.get(myKey));
    }

    @Test
    public void testIncrBy() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        conn.set(myKey, "10".getBytes());
        conn.incrBy(myKey, 5);
        bytesEqual("15".getBytes(), conn.get(myKey));
    }

    @Test
    public void testSetBit() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        assertEquals(0, conn.setBit(myKey, 7, 1));
        assertEquals(1, conn.setBit(myKey, 7, 0));
        final byte[] result = "\u0000".getBytes();
        bytesEqual(result, conn.get(myKey));
    }
}
