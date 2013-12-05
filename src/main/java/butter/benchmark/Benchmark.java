package butter.benchmark;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-18
 * Time: 下午6:44
 */
public class Benchmark {

    public static void main(String[] args) throws Exception {
        BenchmarkConfig config = parseConfig(args);
        System.out.printf(config.toString());
    }

    private static BenchmarkConfig parseConfig(String[] args) {
        BenchmarkConfig config = new BenchmarkConfig();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-h":
                    config.setHost(args[++i]);
                    break;
                case "-p":
                    config.setPort(Integer.parseInt(args[++i]));
                    break;
                case "-c":
                    config.setConnections(Integer.parseInt(args[++i]));
                    break;
                case "-n":
                    config.setRequests(Integer.parseInt(args[++i]));
                    break;
                case "-d":
                    config.setDataSize(Integer.parseInt(args[++i]));
                    break;
                case "-t":
                    config.setTests(args[++i].split(","));
                    break;
                default:
                    showUsage();
            }
        }

        return config;
    }

    private static void showUsage() {
        System.out.printf(
                "Usage: redis-benchmark [-h <host>] [-p <port>] [-c <clients>] [-n <requests]> [-k <boolean>]\n" +
                        " -h <hostname>      Server hostname (default 127.0.0.1)\n" +
                        " -p <port>          Server port (default 6379)\n" +
                        " -s <socket>        Server socket (overrides host and port)\n" +
                        " -c <clients>       Number of parallel connections (default 50)\n" +
                        " -n <requests>      Total number of requests (default 10000)\n" +
                        " -d <size>          Data size of SET/GET value in bytes (default 2)\n" +
                        " -k <boolean>       1=keep alive 0=reconnect (default 1)\n" +
                        " -r <keyspacelen>   Use random keys for SET/GET/INCR, random values for SADD\n" +
                        "  Using this option the benchmark will get/set keys\n" +
                        "  in the form mykey_rand:000000012456 instead of constant\n" +
                        "  keys, the <keyspacelen> argument determines the max\n" +
                        "  number of values for the random number. For instance\n" +
                        "  if set to 10 only rand:000000000000 - rand:000000000009\n" +
                        "  range will be allowed.\n" +
                        " -P <numreq>        Pipeline <numreq> requests. Default 1 (no pipeline).\n" +
                        " -q                 Quiet. Just show query/sec values\n" +
                        " --csv              Output in CSV format\n" +
                        " -l                 Loop. Run the tests forever\n" +
                        " -t <tests>         Only run the comma separated list of tests. The test\n" +
                        "                    names are the same as the ones produced as output.\n" +
                        " -I                 Idle mode. Just open N idle connections and wait.\n" +
                        "Examples:\n" +
                        " Run the benchmark with the default configuration against 127.0.0.1:6379:\n" +
                        "   $ redis-benchmark\n" +
                        " Use 20 parallel clients, for a total of 100k requests, against 192.168.1.1:\n" +
                        "   $ redis-benchmark -h 192.168.1.1 -p 6379 -n 100000 -c 20\n" +
                        " Fill 127.0.0.1:6379 with about 1 million keys only using the SET test:\n" +
                        "   $ redis-benchmark -t set -n 1000000 -r 100000000\n" +
                        " Benchmark 127.0.0.1:6379 for a few commands producing CSV output:\n" +
                        "   $ redis-benchmark -t ping,set,get -n 100000 --csv\n" +
                        " Fill a list with 10000 random elements:\n" +
                        "   $ redis-benchmark -r 10000 -n 10000 lpush mylist ele:rand:000000000000"
        );
    }

//    final int TRY_TIMES = 10000;
//    final AtomicInteger counter = new AtomicInteger();
//    final CountDownLatch latch = new CountDownLatch(1);
//    ExecutorService fixedExecuter = Executors.newSingleThreadExecutor();
//    RedisClient client = new RedisClient(HOST, PORT);
//    client.init();
//    AsyncConnection connection = client.getAsyncConnection();
//    long start = System.currentTimeMillis();
//    for (int i = 0; i < TRY_TIMES; i++) {
//        ListenableFuture<StatusReply> replyFuture = connection.set(KEY.getBytes(), VALUE.getBytes());
//        Futures.addCallback(replyFuture, new FutureCallback<StatusReply>() {
//            @Override
//            public void onSuccess(@Nullable StatusReply result) {
//                int count = counter.incrementAndGet();
//                if(count == TRY_TIMES) {
//                    latch.countDown();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        }, fixedExecuter);
//    }
//
//    latch.await();
//    long end = System.currentTimeMillis();
//    System.out.println(end - start);
//    connection.close();
//    client.shutdown();
//    fixedExecuter.shutdown();
}
