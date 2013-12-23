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

    @Test
    public void testDel() {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] key3 = "key3".getBytes();

        conn.set(key1, "Hello".getBytes());
        conn.set(key2, "World".getBytes());

        assertEquals(2, conn.del(key1, key2, key3));
    }

    @Test
    public void testDump() {
        final byte[] key1 = "key1".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(key1, hello);
        byte[] dump = conn.dump(key1);
        long delNum = conn.del(key1);
        conn.restore(key1, 0, dump);
        assertEquals(1, delNum);
        byte[] myValue = conn.get(key1);
        assertBytesEqual(hello, myValue);
    }

    @Test
    public void testExists() {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(key1, hello);
        assertEquals(1, conn.exists(key1));
        assertEquals(0, conn.exists(key2));
    }

    @Test
    public void testExpire() {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] helloWorld = "Hello World".getBytes();

        conn.set(myKey, hello);
        conn.expire(myKey, 10);
        assertEquals(10, conn.ttl(myKey));
        conn.set(myKey, helloWorld);
        assertEquals(-1, conn.ttl(myKey));
    }

    @Test
    public void testExpireAt() {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        assertEquals(1, conn.exists(myKey));
        conn.expireAt(myKey, 1293840000);
        assertEquals(0, conn.exists(myKey));
    }

    @Test
    public void testKeys() throws Exception {
        final byte[] one = "one".getBytes();
        final byte[] two = "two".getBytes();
        final byte[] three = "three".getBytes();
        final byte[] four = "four".getBytes();

        conn.mset(one, "1".getBytes(), two, "2".getBytes(), three, "3".getBytes(), four, "4".getBytes());
        List<byte[]> keys = conn.keys("*ee*".getBytes());
        assertEquals("three", new String(keys.get(0)));
    }

    @Test
    public void testMove() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        assertEquals(1, conn.move(myKey, 1));
        conn.select(1);
        byte[] myValue = conn.get(myKey);
        assertBytesEqual(hello, myValue);
    }

    @Test
    public void testPersist() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        conn.expire(myKey, 10);
        assertEquals(10, conn.ttl(myKey));
        conn.persist(myKey);
        assertEquals(-1, conn.ttl(myKey));
    }

    @Test
    public void testPexpire() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        conn.pexpire(myKey, 1500);
        //BUG in mac port redis
//        assertEquals(1, conn.ttl(myKey));
//        assertTrue(conn.pttl(myKey) < 1500);
    }

    @Test
    public void testPexpireAt() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        long expireTime = System.currentTimeMillis() + 60 * 1000;
        conn.pexpireAt(myKey, expireTime);
        assertTrue(conn.ttl(myKey) <= 60);
        assertTrue(conn.pttl(myKey) <= 60 * 1000);
    }

    @Test
    public void testRandomKey() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);

        byte[] randKey = conn.randomKey();
        assertEquals(1, conn.exists(randKey));
    }

    @Test
    public void testRename() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] newName = "newName".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);

        conn.rename(myKey, newName);
        byte[] myValue = conn.get(newName);
        assertBytesEqual(hello, myValue);
    }

    @Test
    public void testRenameNX() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] myOtherKey = "newName".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] world = "World".getBytes();
        conn.set(myKey, hello);
        conn.set(myOtherKey, world);

        assertEquals(0, conn.renameNX(myKey, myOtherKey));
        byte[] myValue = conn.get(myOtherKey);
        assertBytesEqual(world, myValue);
    }

    @Test
    public void testType() throws Exception {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] key3 = "key3".getBytes();
        final byte[] key4 = "key4".getBytes();
        final byte[] value = "value".getBytes();

        conn.set(key1, value);
        conn.lpush(key2, value);
        conn.sadd(key3, value);

        assertEquals("string", conn.type(key1));
        assertEquals("list", conn.type(key2));
        assertEquals("set", conn.type(key3));
        assertEquals("none", conn.type(key4));
    }
}
