package butter.benchmark.async;

import butter.connection.AsyncConnection;
import com.google.common.util.concurrent.Futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-12-7
 * Time: 下午2:28
 */
public class AsyncPing extends RecursiveAction {

    private AsyncConnection[] asyncConnections;
    private int connectionIdx = -1;
    private int times;
    private int successTimes;

    public AsyncPing(AsyncConnection[] asyncConnections, int times, int connectionIdx) {
        this.asyncConnections = asyncConnections;
        this.times = times;
        this.connectionIdx = connectionIdx;
    }

    public AsyncPing(AsyncConnection[] asyncConnections, int times) {
        this.asyncConnections = asyncConnections;
        this.times = times;
    }

    @Override
    protected void compute() {
        if (connectionIdx != -1) {
            AsyncConnection conn = asyncConnections[connectionIdx];
            for (int i = 0; i < times; i++) {
                conn.ping();
                //TODO 等待ReplyDecoder重构完成
            }
        } else {
            int part = asyncConnections.length;
            int mod = times % asyncConnections.length;
            int partTimes = times / asyncConnections.length;
            List<AsyncPing> subTasks = new ArrayList<>();
            for (int i = 0; i < part; i++) {
                if (i < part - 1)
                    subTasks.add(new AsyncPing(asyncConnections, partTimes, i));
                else
                    subTasks.add(new AsyncPing(asyncConnections, partTimes + mod, i));
            }
        }
    }


}
