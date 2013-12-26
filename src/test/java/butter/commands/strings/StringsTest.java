package butter.commands.strings;

import butter.commands.RedisTest;
import butter.connection.protocol.BitOPs;
import butter.exception.CommandInterruptedException;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

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
        assertBytesEqual(result, conn.get(myKey));
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

        assertBytesEqual(result, conn.get(dest));
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
        assertBytesEqual(hello, conn.get(myKey));
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
        assertBytesEqual("This".getBytes(), conn.getRange(myKey, 0, 3));
        assertBytesEqual("ing".getBytes(), conn.getRange(myKey, -3, -1));
        assertBytesEqual(value, conn.getRange(myKey, 0, -1));
        assertBytesEqual("string".getBytes(), conn.getRange(myKey, 10, 100));
    }

    @Test
    public void testGetSet() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();

        conn.set(myKey, hello);
        assertBytesEqual(hello, conn.getSet(myKey, world));
        assertBytesEqual(world, conn.get(myKey));
    }

    @Test
    public void testIncr() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        conn.set(myKey, "10".getBytes());
        conn.incr(myKey);
        assertBytesEqual("11".getBytes(), conn.get(myKey));
    }

    @Test
    public void testIncrBy() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        conn.set(myKey, "10".getBytes());
        conn.incrBy(myKey, 5);
        assertBytesEqual("15".getBytes(), conn.get(myKey));
    }

    @Test
    public void testIncrByFloat() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        conn.set(myKey, "10.50".getBytes());
        byte[] result = conn.incrByFloat(myKey, 0.1);
        assertBytesEqual("10.6".getBytes(), result);
        conn.set(myKey, "5.0e3".getBytes());
        result = conn.incrByFloat(myKey, 2.0e2);
        assertBytesEqual("5200".getBytes(), result);
    }

    @Test
    public void testMGet() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();

        conn.set(key1, hello);
        conn.set(key2, world);

        List<byte[]> result = conn.mget(key1, key2, "nonexisting".getBytes());
        assertEquals(3, result.size());
        assertBytesEqual(hello, result.get(0));
        assertBytesEqual(world, result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testMSet() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();

        conn.mset(key1, hello, key2, world);

        assertBytesEqual(hello, conn.get(key1));
        assertBytesEqual(world, conn.get(key2));
    }

    @Test
    public void testMSetNX() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] key3 = "key3".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();
        final byte[] three = "three".getBytes();

        assertEquals(1, conn.msetNX(key1, hello, key2, three));
        assertEquals(0, conn.msetNX(key2, three, key3, world));
        List<byte[]> result = conn.mget(key1, key2, key3);
        assertEquals(3, result.size());
        assertBytesEqual(hello, result.get(0));
        assertBytesEqual(three, result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testPSetEX() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.psetEX(myKey, 1000, hello);
        assertTrue((conn.pttl(myKey) <= 1000));
        assertBytesEqual(hello, conn.get(myKey));
    }

    @Test
    public void testSet() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        assertBytesEqual(hello, conn.get(myKey));
    }

    @Test
    public void testSetBit() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        assertEquals(0, conn.setBit(myKey, 7, 1));
        assertEquals(1, conn.setBit(myKey, 7, 0));
        final byte[] result = "\u0000".getBytes();
        assertBytesEqual(result, conn.get(myKey));
    }

    @Test
    public void testSetEX() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();

        conn.setEX(myKey, 10, hello);
        assertTrue(conn.ttl(myKey) <= 10);
        assertBytesEqual(hello, conn.get(myKey));
    }

    @Test
    public void testSetNX() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();

        assertEquals(1, conn.setNX(myKey, hello));
        assertEquals(0, conn.setNX(myKey, world));
        assertBytesEqual(hello, conn.get(myKey));
    }

    @Test
    public void testSetRange() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] value = "Hello World".getBytes();
        final byte[] result = "Hello Redis".getBytes();

        conn.set(key1, value);
        assertEquals(11, conn.setRange(key1, 6, "Redis".getBytes()));
        assertBytesEqual(result, conn.get(key1));
    }

    @Test
    public void testStrLen() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] value = "Hello World".getBytes();

        conn.set(myKey, value);
        assertEquals(11, conn.strLen(myKey));
        assertEquals(0, conn.strLen("nonexisting".getBytes()));
    }
}
