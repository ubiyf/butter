package butter.commands.zsets;

import butter.commands.RedisTest;
import butter.util.Pair;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

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
}
