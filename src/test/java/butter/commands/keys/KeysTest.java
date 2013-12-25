package butter.commands.keys;

import butter.commands.RedisTest;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-11
 * Time: 下午5:43
 */
public class KeysTest extends RedisTest {
    private static final byte[] KEY1 = "key1".getBytes();
    private static final byte[] KEY2 = "key2".getBytes();
    private static final byte[] KEY3 = "key3".getBytes();
    private static final byte[] MY_KEY = "mykey".getBytes();
    private static final byte[] HELLO = "Hello".getBytes();
    private static final byte[] WORLD = "World".getBytes();
    private static final byte[] ONE = "one".getBytes();
    private static final byte[] TWO = "two".getBytes();
    private static final byte[] THREE = "three".getBytes();
    private static final byte[] FOUR = "four".getBytes();

    @Test
    public void testDel() {
        assertEquals(2, conn.del(KEY1, KEY2, KEY3));
    }

    @Test
    public void testDump() {
        conn.set(KEY1, HELLO);
        byte[] dump = conn.dump(KEY1);
        long delNum = conn.del(KEY1);
        conn.restore(KEY1, 0, dump);
        assertEquals(1, delNum);
        byte[] myValue = conn.get(KEY1);
        assertBytesEqual(HELLO, myValue);
    }

    @Test
    public void testExists() {
        conn.set(KEY1, HELLO);
        assertEquals(1, conn.exists(KEY1));
        assertEquals(0, conn.exists(KEY2));
    }

    @Test
    public void testExpire() {
        final byte[] helloWorld = "Hello World".getBytes();

        conn.set(MY_KEY, HELLO);
        conn.expire(MY_KEY, 10);
        assertEquals(10, conn.ttl(MY_KEY));
        conn.set(MY_KEY, helloWorld);
        assertEquals(-1, conn.ttl(MY_KEY));
    }

    @Test
    public void testExpireAt() {
        conn.set(MY_KEY, HELLO);
        assertEquals(1, conn.exists(MY_KEY));
        conn.expireAt(MY_KEY, 1293840000);
        assertEquals(0, conn.exists(MY_KEY));
    }

    @Test
    public void testKeys() throws Exception {
        conn.mset(ONE, "1".getBytes(), TWO, "2".getBytes(), THREE, "3".getBytes(), FOUR, "4".getBytes());
        List<byte[]> keys = conn.keys("*ee*".getBytes());
        assertEquals("three", new String(keys.get(0)));
    }

    @Test
    public void testMove() throws Exception {
        conn.set(MY_KEY, HELLO);
        assertEquals(1, conn.move(MY_KEY, 1));
        conn.select(1);
        byte[] myValue = conn.get(MY_KEY);
        assertBytesEqual(HELLO, myValue);
    }

    @Test
    public void testPersist() throws Exception {
        conn.set(MY_KEY, HELLO);
        conn.expire(MY_KEY, 10);
        assertEquals(10, conn.ttl(MY_KEY));
        conn.persist(MY_KEY);
        assertEquals(-1, conn.ttl(MY_KEY));
    }

    @Test
    public void testPexpire() throws Exception {
        conn.set(MY_KEY, HELLO);
        conn.pexpire(MY_KEY, 1500);
        //BUG in mac port redis
//        assertEquals(1, conn.ttl(myKey));
//        assertTrue(conn.pttl(myKey) < 1500);
    }

    @Test
    public void testPexpireAt() throws Exception {
        conn.set(MY_KEY, HELLO);
        long expireTime = System.currentTimeMillis() + 60 * 1000;
        conn.pexpireAt(MY_KEY, expireTime);
        assertTrue(conn.ttl(MY_KEY) <= 60);
        assertTrue(conn.pttl(MY_KEY) <= 60 * 1000);
    }

    @Test
    public void testRandomKey() throws Exception {
        conn.set(MY_KEY, HELLO);

        byte[] randKey = conn.randomKey();
        assertEquals(1, conn.exists(randKey));
    }

    @Test
    public void testRename() throws Exception {
        final byte[] newName = "newName".getBytes();
        conn.set(MY_KEY, HELLO);

        conn.rename(MY_KEY, newName);
        byte[] myValue = conn.get(newName);
        assertBytesEqual(HELLO, myValue);
    }

    @Test
    public void testRenameNX() throws Exception {
        final byte[] myOtherKey = "newName".getBytes();
        conn.set(MY_KEY, HELLO);
        conn.set(myOtherKey, MY_KEY);

        assertEquals(0, conn.renameNX(MY_KEY, myOtherKey));
        byte[] myValue = conn.get(myOtherKey);
        assertBytesEqual(WORLD, myValue);
    }

    @Test
    public void testType() throws Exception {
        final byte[] key4 = "key4".getBytes();
        final byte[] value = "value".getBytes();

        conn.set(KEY1, value);
        conn.lpush(KEY2, value);
        conn.sadd(KEY3, value);

        assertEquals("string", conn.type(KEY1));
        assertEquals("list", conn.type(KEY2));
        assertEquals("set", conn.type(KEY3));
        assertEquals("none", conn.type(key4));
    }
}
