package butter.benchmark;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-12-5
 * Time: 下午4:23
 */
public class BenchmarkConfig {
    private static final int DEFAULT_PORT = 6379;
//    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_HOST = "10.235.2.212";
    private static final int DEFAULT_THREAD_NUM = 10;
    private static final int DEFAULT_REQUESTS = 100000;
    private static final int DEFAULT_DATA_SIZE = 2;
    private static final String[] DEFAULT_TESTS = {"PING", "SET", "GET", "INCR"};
    private static final WorkMode DEFAULT_WORK_MODE = WorkMode.ASYNC;
    private static final String DEFAULT_PWD = "xsjmproj";

    private static final boolean DEFAULT_IS_THREAD_SAFE = true;

    private static int port;
    private static String host;
    private static int connections;
    private static int threadNum;
    private static int requests;
    private static int dataSize;
    private static String[] tests;
    private static WorkMode mode;
    private static String pwd;
    private static boolean isThreadSafe;

    static {
        port = DEFAULT_PORT;
        host = DEFAULT_HOST;
        requests = DEFAULT_REQUESTS;
        dataSize = DEFAULT_DATA_SIZE;
        tests = DEFAULT_TESTS;
        mode = DEFAULT_WORK_MODE;
        pwd = DEFAULT_PWD;
        threadNum = DEFAULT_THREAD_NUM;
        isThreadSafe = DEFAULT_IS_THREAD_SAFE;
        if (isThreadSafe) {
            connections = threadNum;
        } else {
            connections = 1;
        }
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int p) {
        if (p <= 0) {
            throw new IllegalArgumentException("invalid port number.");
        }
        port = p;
    }

    public static void setThreadNum(int threadNum) {
        BenchmarkConfig.threadNum = threadNum;
    }

    public static int getThreadNum() {
        return threadNum;
    }

    public static void setThreadSafe(boolean threadSafe) {
        isThreadSafe = threadSafe;
    }

    public static boolean isThreadSafe() {
        return isThreadSafe;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String h) {
        if (h == null) {
            throw new IllegalArgumentException("host can not be null.");
        }
        host = h;
    }

    public static int getConnections() {
        return connections;
    }

    public static void setConnections(int conns) {
        if (conns <= 0) {
            throw new IllegalArgumentException("connection number must be positive.");
        }
        connections = conns;
    }

    public static int getRequests() {
        return requests;
    }

    public static void setRequests(int reqs) {
        requests = reqs;
    }

    public static int getDataSize() {
        return dataSize;
    }

    public static void setDataSize(int size) {
        dataSize = size;
    }

    public static String[] getTests() {
        return tests;
    }

    public static void setTests(String[] ts) {
        tests = ts;
    }

    public static WorkMode getMode() {
        return mode;
    }

    public static void setMode(WorkMode wm) {
        mode = wm;
    }

    public static void setPwd(String pw) {
        pwd = pw;
    }

    public static String getPwd() {
        return pwd;
    }

    @Override
    public String toString() {
        return "BenchmarkConfig{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", connections=" + connections +
                ", requests=" + requests +
                ", dataSize=" + dataSize +
                ", tests=" + Arrays.toString(tests) +
                '}';
    }
}
