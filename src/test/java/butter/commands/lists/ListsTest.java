package butter.commands.lists;

import butter.commands.RedisTest;
import butter.protocol.InsertPos;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-17
 * Time: 上午10:23
 */
public class ListsTest extends RedisTest {

    private static final byte[] MY_LIST = "mylist".getBytes();
    private static final byte[] MY_OTHER_LIST = "myotherlist".getBytes();
    private static final byte[] HELLO = "Hello".getBytes();
    private static final byte[] WORLD = " World".getBytes();
    private static final byte[] ONE = " one".getBytes();
    private static final byte[] TWO = " two".getBytes();
    private static final byte[] THREE = "three".getBytes();

    //TODO testBLPop
    //TODO testBRPop
    //TODO testBRPopLPush
    @Test
    public void testLIndex() throws Exception {
        assertEquals(1, conn.lpush(MY_LIST, WORLD));
        assertEquals(2, conn.lpush(MY_LIST, HELLO));

        bytesEqual(HELLO, conn.lindex(MY_LIST, 0));
        bytesEqual(WORLD, conn.lindex(MY_LIST, -1));
        assertNull(conn.lindex(MY_LIST, 3));
    }

    @Test
    public void testLInsert() throws Exception {
        conn.rpush(MY_LIST, HELLO);
        conn.rpush(MY_LIST, WORLD);

        assertEquals(3, conn.linsert(MY_LIST, InsertPos.BEFORE, WORLD, THREE));

        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(HELLO, results.get(0));
        bytesEqual(THREE, results.get(1));
        bytesEqual(WORLD, results.get(2));
    }

    @Test
    public void testLLen() throws Exception {
        conn.lpush(MY_LIST, WORLD);
        conn.lpush(MY_LIST, HELLO);
        assertEquals(2, conn.llen(MY_LIST));
    }

    @Test
    public void testLPop() throws Exception {
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        bytesEqual(ONE, conn.lpop(MY_LIST));
        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(TWO, results.get(0));
        bytesEqual(THREE, results.get(1));
    }

    @Test
    public void testLPush() throws Exception {
        conn.lpush(MY_LIST, WORLD);
        conn.lpush(MY_LIST, HELLO);
        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(HELLO, results.get(0));
        bytesEqual(WORLD, results.get(1));
    }

    @Test
    public void testLPushX() throws Exception {
        conn.lpush(MY_LIST, WORLD);
        assertEquals(2, conn.lpushX(MY_LIST, HELLO));
        assertEquals(0, conn.lpushX(MY_OTHER_LIST, HELLO));
        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(HELLO, results.get(0));
        bytesEqual(WORLD, results.get(1));

        results = conn.lrange(MY_OTHER_LIST, 0, -1);
        assertNull(results);
    }

    @Test
    public void testLRange() throws Exception {
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        List<byte[]> results = conn.lrange(MY_LIST, 0, 0);
        assertEquals(1, results.size());
        bytesEqual(ONE, results.get(0));

        results = conn.lrange(MY_LIST, -3, 2);
        bytesEqual(ONE, results.get(0));
        bytesEqual(TWO, results.get(1));
        bytesEqual(THREE, results.get(2));

        assertNull(conn.lrange(MY_LIST, 5, 10));
    }

    @Test
    public void testLRem() throws Exception {
        conn.rpush(MY_LIST, HELLO);
        conn.rpush(MY_LIST, HELLO);
        conn.rpush(MY_LIST, "foo".getBytes());
        conn.rpush(MY_LIST, HELLO);

        assertEquals(2, conn.lrem(MY_LIST, -2, HELLO));

        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        assertEquals(2, results.size());
        bytesEqual(HELLO, results.get(0));
        bytesEqual("foo".getBytes(), results.get(1));
    }

    @Test
    public void testLSet() throws Exception {
        final byte[] FOUR = "four".getBytes();
        final byte[] FIVE = "five".getBytes();
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        conn.lset(MY_LIST, 0, FOUR);
        conn.lset(MY_LIST, -2, FIVE);
        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);

        bytesEqual(FOUR, results.get(0));
        bytesEqual(FIVE, results.get(1));
        bytesEqual(THREE, results.get(2));
    }

    @Test
    public void testLTrim() throws Exception {
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        conn.ltrim(MY_LIST, 1, -1);
        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        assertEquals(2, results.size());
        bytesEqual(TWO, results.get(0));
        bytesEqual(THREE, results.get(1));
    }

    @Test
    public void testRPop() throws Exception {
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        byte[] result = conn.rpop(MY_LIST);

        List<byte[]> left = conn.lrange(MY_LIST, 0, -1);
        assertEquals(2, left.size());
        bytesEqual(ONE, left.get(0));
        bytesEqual(TWO, left.get(1));
    }

    @Test
    public void testRPopLPush() throws Exception {
        conn.rpush(MY_LIST, ONE);
        conn.rpush(MY_LIST, TWO);
        conn.rpush(MY_LIST, THREE);

        bytesEqual(THREE, conn.rpopLPush(MY_LIST, MY_OTHER_LIST));
        List<byte[]> left = conn.lrange(MY_LIST, 0, -1);
        assertEquals(2, left.size());
        bytesEqual(ONE, left.get(0));
        bytesEqual(TWO, left.get(1));

        List<byte[]> otherList = conn.lrange(MY_OTHER_LIST, 0, -1);
        assertEquals(1, otherList.size());
        bytesEqual(THREE, otherList.get(0));
    }

    @Test
    public void testRPush() throws Exception {
        conn.rpush(MY_LIST, HELLO);
        conn.rpush(MY_LIST, WORLD);

        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(HELLO, results.get(0));
        bytesEqual(WORLD, results.get(1));
    }

    @Test
    public void testRPushX() throws Exception {
        conn.rpush(MY_LIST, HELLO);
        conn.rpushX(MY_LIST, WORLD);
        conn.rpushX(MY_OTHER_LIST, WORLD);

        List<byte[]> results = conn.lrange(MY_LIST, 0, -1);
        bytesEqual(HELLO, results.get(0));
        bytesEqual(WORLD, results.get(1));

        results = conn.lrange(MY_OTHER_LIST, 0, -1);
        assertNull(results);
    }
}
