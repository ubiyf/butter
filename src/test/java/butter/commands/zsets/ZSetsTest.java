package butter.commands.zsets;

import butter.commands.RedisTest;
import butter.support.Pair;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-18
 * Time: 下午7:42
 */
public class ZSetsTest extends RedisTest {
    private static final byte[] MY_ZSET = "myzset".getBytes();
    private static final byte[] MY_OTHER_ZSET = "myotherzset".getBytes();

    private static final byte[] ONE = "one".getBytes();
    private static final byte[] UNO = "uno".getBytes();
    private static final byte[] TWO = "two".getBytes();
    private static final byte[] THREE = "three".getBytes();
    private static final Pair ONE_PAIR = Pair.of(1d, ONE);
    private static final Pair UNO_PAIR = Pair.of(1d, UNO);
    private static final Pair TWO_PAIR = Pair.of(2d, TWO);
    private static final Pair TWO_PAIR2 = Pair.of(3d, TWO);
    private static final Pair THREE_PAIR = Pair.of(3d, THREE);

    @Test
    public void testZAdd() throws Exception {
        assertEquals(1, conn.zadd(MY_ZSET, ONE_PAIR));
        assertEquals(1, conn.zadd(MY_ZSET, UNO_PAIR));
        assertEquals(1, conn.zadd(MY_ZSET, TWO_PAIR));
        assertEquals(0, conn.zadd(MY_ZSET, TWO_PAIR2));

        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, true);
        assertEquals(6, results.size());
        assertBytesEqual(ONE, results.get(0));
        assertBytesEqual("1".getBytes(), results.get(1));
        assertBytesEqual(UNO, results.get(2));
        assertBytesEqual("1".getBytes(), results.get(3));
        assertBytesEqual(TWO, results.get(4));
        assertBytesEqual("3".getBytes(), results.get(5));
    }

    @Test
    public void testZCard() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);

        assertEquals(2, conn.zcard(MY_ZSET));
    }

    @Test
    public void testZCount() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(3, conn.zcount(MY_ZSET, "-inf".getBytes(), "+inf".getBytes()));
        assertEquals(2, conn.zcount(MY_ZSET, "(1".getBytes(), "3".getBytes()));
    }

    @Test
    public void testZIncrBy() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);

        assertBytesEqual("3".getBytes(), conn.zincrBy(MY_ZSET, 2, ONE));

        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, true);
        assertBytesEqual(TWO, results.get(0));
        assertBytesEqual("2".getBytes(), results.get(1));
        assertBytesEqual(ONE, results.get(2));
        assertBytesEqual("3".getBytes(), results.get(3));
    }

    @Test
    public void testZRange() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, false);
        assertEquals(3, results.size());
        assertBytesEqual(ONE, results.get(0));
        assertBytesEqual(TWO, results.get(1));
        assertBytesEqual(THREE, results.get(2));

        results = conn.zrange(MY_ZSET, 2, 3, false);
        assertEquals(1, results.size());
        assertBytesEqual(THREE, results.get(0));

        results = conn.zrange(MY_ZSET, -2, -1, false);
        assertEquals(2, results.size());
        assertBytesEqual(TWO, results.get(0));
        assertBytesEqual(THREE, results.get(1));
    }

    @Test
    public void testZRangeByScore() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        List<byte[]> results = conn.zrangeByScore(MY_ZSET, "-inf".getBytes(), "+inf".getBytes(), false, 0, 0);

        assertEquals(3, results.size());
        assertBytesEqual(ONE, results.get(0));
        assertBytesEqual(TWO, results.get(1));
        assertBytesEqual(THREE, results.get(2));

        results = conn.zrangeByScore(MY_ZSET, "1".getBytes(), "2".getBytes(), false, 0, 0);
        assertBytesEqual(ONE, results.get(0));
        assertBytesEqual(TWO, results.get(1));

        results = conn.zrangeByScore(MY_ZSET, "(1".getBytes(), "2".getBytes(), false, 0, 0);
        assertBytesEqual(TWO, results.get(0));

        results = conn.zrangeByScore(MY_ZSET, "(1".getBytes(), "(2".getBytes(), false, 0, 0);
        assertNull(results);
    }

    @Test
    public void testZRank() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(2, conn.zrank(MY_ZSET, THREE).longValue());
        assertNull(conn.zrank(MY_ZSET, "four".getBytes()));
    }

    @Test
    public void testZRem() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(1, conn.zrem(MY_ZSET, TWO));

        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, true);
        assertBytesEqual(ONE, results.get(0));
        assertBytesEqual("1".getBytes(), results.get(1));
        assertBytesEqual(THREE, results.get(2));
        assertBytesEqual("3".getBytes(), results.get(3));
    }

    @Test
    public void testZRemRangeByRank() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(2, conn.zremRangeByRank(MY_ZSET, 0, 1));
        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, true);
        assertBytesEqual(THREE, results.get(0));
        assertBytesEqual("3".getBytes(), results.get(1));
    }

    @Test
    public void testZRemRangeByScore() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(1, conn.zremRangeByScore(MY_ZSET, "-inf".getBytes(), "(2".getBytes()));
        List<byte[]> results = conn.zrange(MY_ZSET, 0, -1, true);
        assertBytesEqual(TWO, results.get(0));
        assertBytesEqual("2".getBytes(), results.get(1));
        assertBytesEqual(THREE, results.get(2));
        assertBytesEqual("3".getBytes(), results.get(3));
    }

    @Test
    public void testZRevRange() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        List<byte[]> results = conn.zrevRange(MY_ZSET, 0, -1, false);
        assertBytesEqual(THREE, results.get(0));
        assertBytesEqual(TWO, results.get(1));
        assertBytesEqual(ONE, results.get(2));

        results = conn.zrevRange(MY_ZSET, 2, 3, false);
        assertBytesEqual(ONE, results.get(0));

        results = conn.zrevRange(MY_ZSET, -2, -1, false);
        assertBytesEqual(TWO, results.get(0));
        assertBytesEqual(ONE, results.get(1));
    }

    @Test
    public void testZRevRangeByScore() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        List<byte[]> results = conn.zrevRangeByScore(MY_ZSET, "+inf".getBytes(), "-inf".getBytes(), false, 0, 0);
        assertBytesEqual(THREE, results.get(0));
        assertBytesEqual(TWO, results.get(1));
        assertBytesEqual(ONE, results.get(2));

        results = conn.zrevRangeByScore(MY_ZSET, "2".getBytes(), "1".getBytes(), false, 0, 0);
        assertBytesEqual(TWO, results.get(0));
        assertBytesEqual(ONE, results.get(1));

        results = conn.zrevRangeByScore(MY_ZSET, "2".getBytes(), "(1".getBytes(), false, 0, 0);
        assertBytesEqual(TWO, results.get(0));

        assertNull(conn.zrevRangeByScore(MY_ZSET, "(2".getBytes(), "(1".getBytes(), false, 0, 0));
    }

    @Test
    public void testZRevRank() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        conn.zadd(MY_ZSET, TWO_PAIR);
        conn.zadd(MY_ZSET, THREE_PAIR);

        assertEquals(2, conn.zrevRank(MY_ZSET, ONE).longValue());
        assertNull(conn.zrevRank(MY_ZSET, "four".getBytes()));
    }

    @Test
    public void testZScore() throws Exception {
        conn.zadd(MY_ZSET, ONE_PAIR);
        assertBytesEqual("1".getBytes(), conn.zscore(MY_ZSET, ONE));
    }
}
