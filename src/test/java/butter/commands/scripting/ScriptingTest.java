package butter.commands.scripting;

import butter.commands.RedisTest;
import butter.exception.CommandInterruptedException;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 12/23/13
 * Time: 8:50 PM
 */
public class ScriptingTest extends RedisTest {
    private static final byte[] KEY1 = "key1".getBytes();
    private static final byte[] KEY2 = "key2".getBytes();
    private static final byte[] FIRST = "first".getBytes();
    private static final byte[] SECOND = "second".getBytes();

    @Test
    public void testEval() throws Exception {
        byte[] script = "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}".getBytes();
        byte[][] keys = {KEY1, KEY2};
        List<byte[]> results = conn.eval(script, keys, "first".getBytes(), "second".getBytes());

        assertBytesEqual(KEY1, results.get(0));
        assertBytesEqual(KEY2, results.get(1));
        assertBytesEqual(FIRST, results.get(2));
        assertBytesEqual(SECOND, results.get(3));

        long intResult = conn.eval("return 10".getBytes(), null);
        assertEquals(10, intResult);

        List<Object> listResults = conn.eval("return {1,2,{3,'Hello World!', {4, 'Hello MOTO!'}}}".getBytes(), null);

        assertEquals(1, (long) listResults.get(0));
        assertEquals(2, (long) listResults.get(1));
        List<Object> subList = (List<Object>) listResults.get(2);
        assertEquals(3, (long) subList.get(0));
        assertBytesEqual("Hello World!".getBytes(), (byte[]) subList.get(1));
        List<Object> subSubList = (List<Object>) subList.get(2);
        assertEquals(4, (long) subSubList.get(0));
        assertBytesEqual("Hello MOTO!".getBytes(), (byte[]) subSubList.get(1));
    }

    @Test
    public void testEvalSHA() throws Exception {
        byte[] sha = conn.scriptLoad("return 'Hello World!'".getBytes());
        byte[] result = conn.evalSHA(sha, null);

        assertBytesEqual("Hello World!".getBytes(), result);
    }

    @Test
    public void testScriptExists() throws Exception {
        byte[] sha = conn.scriptLoad("return 'Hello World!'".getBytes());
        List<Long> results = conn.scriptExists(sha);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).longValue());

        conn.scriptFlush();
        results = conn.scriptExists(sha);
        assertEquals(1, results.size());
        assertEquals(0, results.get(0).longValue());
    }

    @Test
    public void testScriptKill() throws Exception {
        expectedEx.expect(CommandInterruptedException.class);
        conn.scriptKill();
    }
}
