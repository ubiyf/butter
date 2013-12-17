package butter.commands.hashes;

import butter.commands.RedisTest;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-16
 * Time: 下午5:46
 */
public class HashesTest extends RedisTest {

    private static final byte[] MY_HASH = "myhash".getBytes();
    private static final byte[] FIELD = "field".getBytes();
    private static final byte[] FIELD1 = "field1".getBytes();
    private static final byte[] FIELD2 = "field2".getBytes();
    private static final byte[] NO_FIELD = "nofield".getBytes();
    private static final byte[] HELLO = "Hello".getBytes();
    private static final byte[] WORLD = "World".getBytes();
    private static final byte[] FOO = "foo".getBytes();

    @Test
    public void testHDel() throws Exception {
        assertEquals(1, conn.hset(MY_HASH, FIELD1, FOO));
        assertEquals(1, conn.hdel(MY_HASH, FIELD1));
        assertEquals(0, conn.hdel(MY_HASH, FIELD2));
    }

    @Test
    public void testHExists() throws Exception {
        conn.hset(MY_HASH, FIELD1, FOO);
        assertEquals(1, conn.hexists(MY_HASH, FIELD1));
        assertEquals(0, conn.hexists(MY_HASH, FIELD2));
    }

    @Test
    public void testHGet() throws Exception {
        conn.hset(MY_HASH, FIELD1, FOO);
        assertBytesEqual(FOO, conn.hget(MY_HASH, FIELD1));
        assertNull(conn.hget(MY_HASH, FIELD2));
    }

    @Test
    public void testHGetAll() throws Exception {
        conn.hset(MY_HASH, FIELD1, HELLO);
        conn.hset(MY_HASH, FIELD2, WORLD);

        List<byte[]> results = conn.hgetAll(MY_HASH);
        assertBytesEqual(FIELD1, results.get(0));
        assertBytesEqual(HELLO, results.get(1));
        assertBytesEqual(FIELD2, results.get(2));
        assertBytesEqual(WORLD, results.get(3));
    }

    @Test
    public void testHIncrBy() throws Exception {
        conn.hset(MY_HASH, FIELD, "5".getBytes());
        assertEquals(6, conn.hincrBy(MY_HASH, FIELD, 1));
        assertEquals(5, conn.hincrBy(MY_HASH, FIELD, -1));
        assertEquals(-5, conn.hincrBy(MY_HASH, FIELD, -10));
    }

    @Test
    public void testHIncrByFloat() throws Exception {
        conn.hset(MY_HASH, FIELD, "10.50".getBytes());
        assertBytesEqual("10.6".getBytes(), conn.hincrByFloat(MY_HASH, FIELD, 0.1));
        conn.hset(MY_HASH, FIELD, "5.0e3".getBytes());
        assertBytesEqual("5200".getBytes(), conn.hincrByFloat(MY_HASH, FIELD, 2.0e2));
    }

    @Test
    public void testHKeys() throws Exception {
        conn.hset(MY_HASH, FIELD1, HELLO);
        conn.hset(MY_HASH, FIELD2, WORLD);

        List<byte[]> keys = conn.hkeys(MY_HASH);
        assertBytesEqual(FIELD1, keys.get(0));
        assertBytesEqual(FIELD2, keys.get(1));
    }

    @Test
    public void testHLen() throws Exception {
        conn.hset(MY_HASH, FIELD1, HELLO);
        conn.hset(MY_HASH, FIELD2, WORLD);

        assertEquals(2, conn.hlen(MY_HASH));
    }

    @Test
    public void testHMGet() throws Exception {
        conn.hset(MY_HASH, FIELD1, HELLO);
        conn.hset(MY_HASH, FIELD2, WORLD);
        List<byte[]> results = conn.hmget(MY_HASH, FIELD1, FIELD2, NO_FIELD);
        assertBytesEqual(HELLO, results.get(0));
        assertBytesEqual(WORLD, results.get(1));
        assertNull(results.get(2));
    }

    @Test
    public void testHMSet() throws Exception {
        conn.hmset(MY_HASH, FIELD1, HELLO, FIELD2, WORLD);
        assertBytesEqual(HELLO, conn.hget(MY_HASH, FIELD1));
        assertBytesEqual(WORLD, conn.hget(MY_HASH, FIELD2));
    }

    @Test
    public void testHSet() throws Exception {
        assertEquals(1, conn.hset(MY_HASH, FIELD1, HELLO));
        assertBytesEqual(HELLO, conn.hget(MY_HASH, FIELD1));
    }

    @Test
    public void testHSetNX() throws Exception {
        assertEquals(1, conn.hsetNX(MY_HASH, FIELD, HELLO));
        assertEquals(0, conn.hsetNX(MY_HASH, FIELD, WORLD));
        assertBytesEqual(HELLO, conn.hget(MY_HASH, FIELD));
    }

    @Test
    public void testHVals() throws Exception {
        conn.hset(MY_HASH, FIELD1, HELLO);
        conn.hset(MY_HASH, FIELD2, WORLD);

        List<byte[]> values = conn.hvals(MY_HASH);
        assertBytesEqual(HELLO, values.get(0));
        assertBytesEqual(WORLD, values.get(1));
    }
}
