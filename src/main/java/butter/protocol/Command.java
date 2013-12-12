package butter.protocol;

import butter.exception.CommandInterruptedException;
import com.google.common.util.concurrent.AbstractFuture;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static butter.util.NumberUtil.stringSize;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:17
 */
public class Command<T> extends AbstractFuture<T> {
    private static final byte[] CRLF = "\r\n".getBytes(Charsets.ASCII);
    private List<byte[]> args = new ArrayList<>();
    private Type type;

    public static <T> Command<T> create(Type type) {
        return new Command<>(type);
    }

    private Command() {
    }

    private Command(Type type) {
        this.type = type;
    }

    @Override
    public T get() {
        T value;
        try {
            value = super.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CommandInterruptedException(e);
        }
        return value;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws TimeoutException {
        T value;
        try {
            value = super.get(timeout, unit);
        } catch (InterruptedException | ExecutionException e) {
            throw new CommandInterruptedException(e);
        }
        return value;
    }

    @Override
    public boolean set(@Nullable T value) {
//        if (!isTypeSafe(value)) {
//            setException(new RedisException("wrong type reply, expected " + type + " got " + value.getClass()));
//            return false;
//        } else {
//            return super.set(value);
//        }

        return super.set(value);
    }

//    private boolean isTypeSafe(T value) {
//        if(value == null)
//            return true;
//        if(type == List.class) {
//            return value.getClass().isInstance(type);
//        }
//
//        return type == value.getClass();
//    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }

    public void addArg(byte[]... args) {
        for (byte[] arg : args) {
            this.args.add(arg);
        }
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
}
