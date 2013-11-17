package butter;

import butter.connection.RedisConnection;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-11-15
 * Time: 下午10:16
 */
public class RedisClientTest {
    private static final int PORT = 6379;
    private static final String HOST = "127.0.0.1";
    private static final String KEY = "mykey";
    private static final String VALUE = "myvalue";

    @Test
    public void testRedisClient() throws Exception {
        RedisClient client = new RedisClient(HOST, PORT);
        client.init();
        RedisConnection connection = client.connect();
        connection.set(KEY.getBytes(), VALUE.getBytes());
    }

    public static void main(String[] args) throws Exception {
        RedisClient client = new RedisClient(HOST, PORT);
        client.init();
        RedisConnection connection = client.connect();
        connection.set(KEY.getBytes(), VALUE.getBytes());
    }
}
