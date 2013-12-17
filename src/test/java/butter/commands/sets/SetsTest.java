package butter.commands.sets;

import butter.commands.RedisTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-17
 * Time: 下午2:31
 */
public class SetsTest extends RedisTest {
    private static final byte[] MY_SET = "myset".getBytes();
    private static final byte[] MY_OTHER_SET = "myotherset".getBytes();
    private static final byte[] KEY1 = "key1".getBytes();
    private static final byte[] KEY2 = "key2".getBytes();
    private static final byte[] HELLO = "Hello".getBytes();
    private static final byte[] WORLD = " World".getBytes();
    private static final byte[] ONE = " one".getBytes();
    private static final byte[] TWO = " two".getBytes();
    private static final byte[] THREE = "three".getBytes();

    private boolean containsBytes(byte[] expected, List<byte[]> actuals) {
        for (byte[] actual : actuals) {
            if (isBytesEqual(expected, actual)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testSAdd() throws Exception {
        assertEquals(1, conn.sadd(MY_SET, HELLO));
        assertEquals(1, conn.sadd(MY_SET, WORLD));
        assertEquals(0, conn.sadd(MY_SET, WORLD));

        List<byte[]> members = conn.smembers(MY_SET);
        assertTrue(containsBytes(HELLO, members));
        assertTrue(containsBytes(WORLD, members));
    }

    @Test
    public void testSCard() throws Exception {
        conn.sadd(MY_SET, HELLO);
        conn.sadd(MY_SET, WORLD);
        assertEquals(2, conn.scard(MY_SET));
    }

    @Test
    public void testSDiff() throws Exception {
        conn.sadd(KEY1, "a".getBytes());
        conn.sadd(KEY1, "b".getBytes());
        conn.sadd(KEY1, "c".getBytes());
        conn.sadd(KEY2, "c".getBytes());
        conn.sadd(KEY2, "d".getBytes());
        conn.sadd(KEY2, "e".getBytes());

        List<byte[]> diff = conn.sdiff(KEY1, KEY2);
        assertEquals(2, diff.size());
        assertTrue(containsBytes("b".getBytes(), diff));
        assertTrue(containsBytes("a".getBytes(), diff));
    }

    @Test
    public void testSDiffStore() throws Exception {
        conn.sadd(KEY1, "a".getBytes());
        conn.sadd(KEY1, "b".getBytes());
        conn.sadd(KEY1, "c".getBytes());
        conn.sadd(KEY2, "c".getBytes());
        conn.sadd(KEY2, "d".getBytes());
        conn.sadd(KEY2, "e".getBytes());

        assertEquals(2, conn.sdiffStore(MY_OTHER_SET, KEY1, KEY2));
        List<byte[]> members = conn.smembers(MY_OTHER_SET);
        assertTrue(containsBytes("b".getBytes(), members));
        assertTrue(containsBytes("a".getBytes(), members));
    }

    @Test
    public void testSInter() throws Exception {
        conn.sadd(KEY1, "a".getBytes());
        conn.sadd(KEY1, "b".getBytes());
        conn.sadd(KEY1, "c".getBytes());
        conn.sadd(KEY2, "c".getBytes());
        conn.sadd(KEY2, "d".getBytes());
        conn.sadd(KEY2, "e".getBytes());

        List<byte[]> inter = conn.sinter(KEY1, KEY2);
        assertEquals(1, inter.size());
        assertTrue(containsBytes("c".getBytes(), inter));
    }

    @Test
    public void testSInterStore() throws Exception {
        conn.sadd(KEY1, "a".getBytes());
        conn.sadd(KEY1, "b".getBytes());
        conn.sadd(KEY1, "c".getBytes());
        conn.sadd(KEY2, "c".getBytes());
        conn.sadd(KEY2, "d".getBytes());
        conn.sadd(KEY2, "e".getBytes());

        assertEquals(1, conn.sinterStore(MY_OTHER_SET, KEY1, KEY2));
        List<byte[]> inter = conn.smembers(MY_OTHER_SET);
        assertEquals(1, inter.size());
        assertTrue(containsBytes("c".getBytes(), inter));
    }

    @Test
    public void testSIsMember() throws Exception {
        conn.sadd(MY_SET, ONE);
        assertEquals(1, conn.sisMember(MY_SET, ONE));
        assertEquals(0, conn.sisMember(MY_SET, TWO));
    }

    @Test
    public void testSMembers() throws Exception {
        conn.sadd(MY_SET, HELLO);
        conn.sadd(MY_SET, WORLD);

        List<byte[]> members = conn.smembers(MY_SET);
        assertTrue(containsBytes(HELLO, members));
        assertTrue(containsBytes(WORLD, members));
    }

    @Test
    public void testSMove() throws Exception {
        conn.sadd(MY_SET, ONE);
        conn.sadd(MY_SET, TWO);
        conn.sadd(MY_OTHER_SET, THREE);

        conn.smove(MY_SET, MY_OTHER_SET, TWO);
        List<byte[]> members = conn.smembers(MY_SET);
        assertEquals(1, members.size());
        assertTrue(containsBytes(ONE, members));

        members = conn.smembers(MY_OTHER_SET);
        assertEquals(2, members.size());
        assertTrue(containsBytes(TWO, members));
        assertTrue(containsBytes(THREE, members));
    }

    @Test
    public void testSPop() throws Exception {
        conn.sadd(MY_SET, ONE);
        conn.sadd(MY_SET, TWO);
        conn.sadd(MY_SET, THREE);

        byte[] member = conn.spop(MY_SET);
        List<byte[]> members = conn.smembers(MY_SET);
        assertEquals(2, members.size());
        List<byte[]> actuals = new ArrayList<>();
        actuals.add(ONE);
        actuals.add(TWO);
        actuals.add(THREE);
        assertTrue(containsBytes(member, actuals));
    }

    @Test
    public void testSrandMember() throws Exception {
        conn.sadd(MY_SET, ONE, TWO, THREE);
        byte[] randMember = conn.srandMember(MY_SET);
        List<byte[]> actuals = new ArrayList<>();
        actuals.add(ONE);
        actuals.add(TWO);
        actuals.add(THREE);
        assertTrue(containsBytes(randMember, actuals));
        List<byte[]> randMembers = conn.srandMember(MY_SET, 2);
        assertEquals(2, randMembers.size());
        randMembers = conn.srandMember(MY_SET, -5);
        assertEquals(5, randMembers.size());
    }

    @Test
    public void testSRem() throws Exception {
        conn.sadd(MY_SET, ONE, TWO, THREE);
        assertEquals(1, conn.srem(MY_SET, ONE));
        assertEquals(0, conn.srem(MY_SET, "four".getBytes()));
        List<byte[]> members = conn.smembers(MY_SET);
        assertEquals(2, members.size());
        assertTrue(containsBytes(TWO, members));
        assertTrue(containsBytes(THREE, members));
    }

    @Test
    public void testSUnion() throws Exception {
        byte[] a = "a".getBytes();
        byte[] b = "b".getBytes();
        byte[] c = "c".getBytes();
        byte[] d = "d".getBytes();
        byte[] e = "e".getBytes();
        conn.sadd(KEY1, a, b, c);
        conn.sadd(KEY2, c, d, e);
        List<byte[]> results = conn.sunion(KEY1, KEY2);
        assertEquals(5, results.size());
        assertTrue(containsBytes(a, results));
        assertTrue(containsBytes(b, results));
        assertTrue(containsBytes(c, results));
        assertTrue(containsBytes(d, results));
        assertTrue(containsBytes(e, results));
    }

    @Test
    public void testSUnionStore() throws Exception {
        byte[] a = "a".getBytes();
        byte[] b = "b".getBytes();
        byte[] c = "c".getBytes();
        byte[] d = "d".getBytes();
        byte[] e = "e".getBytes();
        conn.sadd(KEY1, a, b, c);
        conn.sadd(KEY2, c, d, e);

        assertEquals(5, conn.sunionStore(MY_OTHER_SET, KEY1, KEY2));
        List<byte[]> results = conn.smembers(MY_OTHER_SET);
        assertEquals(5, results.size());
        assertTrue(containsBytes(a, results));
        assertTrue(containsBytes(b, results));
        assertTrue(containsBytes(c, results));
        assertTrue(containsBytes(d, results));
        assertTrue(containsBytes(e, results));
    }
}
