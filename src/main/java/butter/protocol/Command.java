package butter.protocol;

import butter.exception.CommandInterruptedException;
import butter.exception.CommandTimeoutException;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:17
 */
public class Command<T extends Reply> implements Future<T> {
    private static final byte[] CRLF = "\r\n".getBytes(Charsets.ASCII);

    private List<byte[]> args = new ArrayList<byte[]>();
    private CountDownLatch latch = new CountDownLatch(1);
    private T reply;

    public void addArg(byte[] arg) {
        args.add(arg);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning) {
            throw new IllegalArgumentException("once committed, redis command can not be canceled");
        }
        boolean isCanceled = false;
        if (latch.getCount() == 1) {
            latch.countDown();
            reply = null;
            isCanceled = true;
        }
        return isCanceled;
    }

    @Override
    public boolean isCancelled() {
        return latch.getCount() == 0 && reply == null;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public T get() {
        try {
            latch.await();
            return reply;
        } catch (InterruptedException e) {
            throw new CommandInterruptedException(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) {
        try {
            if (!latch.await(timeout, unit)) {
                throw new CommandTimeoutException("Command timed out");
            }
        } catch (InterruptedException e) {
            throw new CommandInterruptedException(e);
        }
        return reply;
    }

    public T getReply() {
        return get();
    }

    public void setReply(T reply) {
        this.reply = reply;
        latch.countDown();
    }

    public void encode(ByteBuf buf) {
        buf.clear();
        buf.writeByte('*');
        write(buf, args.size());
        buf.writeBytes(CRLF);
        for (byte[] arg : args) {
            buf.writeByte('$');
            write(buf, arg.length);
            buf.writeBytes(CRLF);
            buf.writeBytes(arg);
            buf.writeBytes(CRLF);
        }
    }

    /**
     * 将数字以char字符形式写入buffer
     *
     * @param buf
     * @param value max number: 512 * 1024 * 1024
     */
    private void write(ByteBuf buf, int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be positive.");
        }

        if (value > 512 * 1024 * 1204) {
            throw new IllegalArgumentException("data length can not larger than 512MB.");
        }

        if (value < 10) {
            buf.writeByte((byte) ('0' + value));
            return;
        }

        int valueLen = stringSize(value);
        buf.writeZero(valueLen);
        int writeIdx = buf.writerIndex();
        for (int i = 0; i < valueLen; i++) {
            long digit = value % 10;
            buf.setByte(writeIdx - i - 1, (byte) ('0' + digit));
            value = value / 10;
        }
    }

    private final static int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    // Requires positive x
    private static int stringSize(int x) {
        for (int i = 0; ; i++)
            if (x <= SIZE_TABLE[i])
                return i + 1;
    }
}
