package butter.commands;

import butter.RedisClient;
import butter.connection.SyncConnection;
import org.junit.Before;
import org.junit.BeforeClass;

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

}
