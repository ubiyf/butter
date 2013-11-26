package butter.misc;

import com.google.common.util.concurrent.*;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-11-26
 * Time: 下午2:54
 */
public class ListenableFutureTest {

    class CallbackPrinter<V> implements FutureCallback<V> {

        @Override
        public void onSuccess(V result) {
            System.out.println(result);
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testCallBack() throws Exception {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Integer> explosion = service.submit(new Callable<Integer>() {
            private static final int SLEEP_TIME = 1000;

            public Integer call() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return SLEEP_TIME;
            }
        });
        Futures.addCallback(explosion, new CallbackPrinter<Integer>());
        service.awaitTermination(1500, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSettableFuture() throws Exception {
        SettableFuture<Integer> future = SettableFuture.create();
        Futures.addCallback(future, new CallbackPrinter<Integer>());
        Thread.sleep(1000);
        future.set(100);
    }


    class Task1 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("task1 begin");
            Thread.sleep(2000);
            System.out.println("task1 end");
            return new Random().nextInt();
        }
    }

    //add suffix to an integer
    class Task2 implements Callable<String> {

        private Integer i;

        public Task2(Integer i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            System.out.println("task2 begin");
            Thread.sleep(4000);
            System.out.println("task2 end");
            return i + "suffix";
        }
    }

    @Test
    public void testTransform() throws Exception {
        ExecutorService normalService = Executors.newFixedThreadPool(100);
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(normalService);

        ListenableFuture<Integer> future1 = service.submit(new Task1());
        AsyncFunction<Integer, String> asyncFunction = new AsyncFunction<Integer, String>() {
            @Override
            public ListenableFuture<String> apply(Integer input) throws Exception {
                return service.submit(new Task2(input));
            }
        };
        ListenableFuture<String> futures2 = Futures.transform(future1, asyncFunction);
        System.out.println(futures2.get());
    }
}
