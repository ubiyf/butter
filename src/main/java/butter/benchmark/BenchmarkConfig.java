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
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_CONNECTIONS = 50;
    private static final int DEFAULT_REQUESTS = 10000;
    private static final int DEFAULT_DATA_SIZE = 2;
    private static final String[] DEFAULT_TESTS = {"PING", "SET", "GET", "INCR"};

    private int port;
    private String host;
    private int connections;
    private int requests;
    private int dataSize;
    private String[] tests;

    public BenchmarkConfig() {
        this.port = DEFAULT_PORT;
        this.host = DEFAULT_HOST;
        this.connections = DEFAULT_CONNECTIONS;
        this.requests = DEFAULT_REQUESTS;
        this.dataSize = DEFAULT_DATA_SIZE;
        this.tests = DEFAULT_TESTS;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public String[] getTests() {
        return tests;
    }

    public void setTests(String[] tests) {
        this.tests = tests;
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
