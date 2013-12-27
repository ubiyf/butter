package butter.benchmark;

import butter.connection.AsyncConnection;
import butter.connection.protocol.Command;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yangfan
 * Date: 13-12-25
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public class AsynConnTestThread extends Thread {

    private final AsyncConnection aConn;

    private ArrayList<Command> cList = new ArrayList<>(BenchmarkConfig.getRequests());

    private String cmd;

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public AsynConnTestThread(AsyncConnection ac) {
        this.aConn = ac;
    }

    public void run() {
        aConn.auth(BenchmarkConfig.getPwd().getBytes());
        switch (cmd) {
            case "PING":
                doLoopPing();
                break;
            case "SET":
                doLoopSet();
                break;
            case "INCR":
                doLoopIncr();
                break;
            case "GET":
                doLoopGet();
                break;
            default:
                break;
        }
    }

    private void doLoopSet() {
        String key = "test_set_key_" + Thread.currentThread().getName();
        String value = "hello,world!";

        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            Command<String> tmp = aConn.set(key.getBytes(), value.getBytes());
            cList.add(tmp);
        }
        validateFuture();
    }

    private void doLoopGet() {
        String key = "test_set_key_" + Thread.currentThread().getName();
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            Command<byte[]> tmp = aConn.get(key.getBytes());
            cList.add(tmp);
        }
        validateFuture();
    }

    private void doLoopIncr() {
        String key = "test_incr_key_" + Thread.currentThread().getName();
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            Command<Long> tmp = aConn.incr(key.getBytes());
            cList.add(tmp);
        }
        validateFuture();
    }

    private void doLoopPing() {
        for (int i = 0; i< BenchmarkConfig.getRequests(); i++) {
            Command<String> tmp = aConn.ping();
            cList.add(tmp);
        }
        validateFuture();
    }

    private void validateFuture() {
        for (Command c : cList) {
            while (!c.isDone()) {
            }
        }
    }

}
