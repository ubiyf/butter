package butter.commands;

import butter.RedisClient;
import butter.connection.SyncConnection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-11
 * Time: 下午5:42
 */
public abstract class RedisTest {
    public static final int PORT = 6379;
    public static final String HOST = "127.0.0.1";

    protected static RedisClient client;
    protected static SyncConnection conn;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void init() throws Exception {
        client = new RedisClient(HOST, PORT);
        client.init();
        conn = client.getSyncConnection();
        conn.ping();
    }

    @Before
    public void flushDB() {
        conn.flushAll();
    }

    public void assertBytesEqual(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    public boolean isBytesEqual(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < actual.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }

        return true;
    }
}
