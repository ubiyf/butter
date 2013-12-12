package butter.commands.keys;

import butter.commands.RedisTest;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

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

        Assert.assertEquals(2, conn.del(key1, key2, key3));
    }

    @Test
    public void testDump() {
        final byte[] key1 = "key1".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(key1, hello);
        byte[] dump = conn.dump(key1);
        long delNum = conn.del(key1);
        conn.restore(key1, 0, dump);
        Assert.assertEquals(1, delNum);
        byte[] value = conn.get(key1);
        for (int i = 0; i < value.length; i++)
            Assert.assertEquals(value[i], hello[i]);
    }

    @Test
    public void testExists() {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(key1, hello);
        Assert.assertEquals(1, conn.exists(key1));
        Assert.assertEquals(0, conn.exists(key2));
    }

    @Test
    public void testExpire() {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        final byte[] helloWorld = "Hello World".getBytes();

        conn.set(myKey, hello);
        conn.expire(myKey, 10);
        Assert.assertEquals(10, conn.ttl(myKey));
        conn.set(myKey, helloWorld);
        Assert.assertEquals(-1, conn.ttl(myKey));
    }

    @Test
    public void testExpireAt() {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        Assert.assertEquals(1, conn.exists(myKey));
        conn.expireAt(myKey, 1293840000);
        Assert.assertEquals(0, conn.exists(myKey));
    }

    @Test
    public void testKeys() throws Exception {
        final byte[] one = "one".getBytes();
        final byte[] two = "two".getBytes();
        final byte[] three = "three".getBytes();
        final byte[] four = "four".getBytes();

        conn.mset(one, "1".getBytes(), two, "2".getBytes(), three, "3".getBytes(), four, "4".getBytes());
        List<byte[]> keys = conn.keys("*ee*".getBytes());
        Assert.assertEquals("three", new String(keys.get(0)));
    }

    @Test
    public void testMove() throws Exception {
        final byte[] myKey = "mykey".getBytes();
        final byte[] hello = "Hello".getBytes();
        conn.set(myKey, hello);
        Assert.assertEquals(1, conn.move(myKey, 1));
        conn.select(1);
        byte[] myValue = conn.get(myKey);
        for (int i = 0; i < myValue.length; i++) {
            Assert.assertEquals(hello[i], myValue[i]);
        }
    }
}
