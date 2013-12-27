package butter.benchmark;

import butter.connection.SyncConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Yangfan
 * Date: 13-12-25
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public class SyncConnTestThread implements Runnable {

    private final SyncConnection sConn;

    private String cmd;

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public SyncConnTestThread(SyncConnection sc) {
        this.sConn = sc;
    }

    public void run() {
        sConn.auth(BenchmarkConfig.getPwd().getBytes());
        switch (cmd) {
            case "PING":
                doLoopPing();
                break;
            case "SET":
                doLoopSet();
                break;
            case "GET":
                doLoopGet();
                break;
            case "INCR":
                doLoopIncr();
                break;
            default:
                break;
        }
    }

    private void doLoopSet() {
        String key = "test_set_key_" + Thread.currentThread().getName();
        String value = "hello,world!";
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            sConn.set(key.getBytes(), value.getBytes());
        }
    }

    private void doLoopIncr() {
        String key = "test_incr_key_" + Thread.currentThread().getName();
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            sConn.incr(key.getBytes());
        }
    }

    private void doLoopGet() {
        String key = "test_set_key_" + Thread.currentThread().getName();
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            sConn.get(key.getBytes());
        }
    }

    private void doLoopPing() {
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            sConn.ping();
        }
    }

}
