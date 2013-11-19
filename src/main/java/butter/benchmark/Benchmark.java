package butter.benchmark;

import butter.RedisClient;
import butter.connection.AsyncConnection;
import butter.protocol.replies.StatusReply;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-18
 * Time: 下午6:44
 */
public class Benchmark {
    private static final int PORT = 6379;
    private static final String HOST = "127.0.0.1";
    private static final String KEY = "mykey";
    private static final String VALUE = "myvalue";

    public static void main(String[] args) throws Exception {
        final int TRY_TIMES = 100000;
        List<Future<StatusReply>> replies = new ArrayList<Future<StatusReply>>(TRY_TIMES);
        RedisClient client = new RedisClient(HOST, PORT);
        client.init();
        AsyncConnection connection = client.getAsyncConnection();
        long start = System.currentTimeMillis();
        int successedTimes = 0;
        for (int i = 0; i < TRY_TIMES; i++) {
            replies.add(connection.set(KEY.getBytes(), VALUE.getBytes()));
        }

        while (true) {
            Iterator<Future<StatusReply>> it = replies.iterator();
            while (it.hasNext()) {
                it.next().isDone();
                it.remove();
                successedTimes++;
            }
            if (successedTimes == TRY_TIMES)
                break;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        connection.close();
        client.shutdown();
    }
}
