package butter.protocol;

import butter.exception.CommandInterruptedException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:17
 */
public class Command {
    private static final byte[] CRLF = "\r\n".getBytes(Charset.forName("US-ASCII"));

    private List<byte[]> args = new ArrayList<byte[]>();
    private Reply reply;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void addArg(byte[] arg) {
        args.add(arg);
    }

    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
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
        byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(0, data);
        return data;
    }

    private void write(ByteBuf buf, long value) {
        if (value < 10) {
            buf.writeByte((byte) ('0' + value));
            return;
        }

        StringBuilder sb = new StringBuilder(8);
        while (value > 0) {
            long digit = value % 10;
            sb.append((char) ('0' + digit));
            value /= 10;
        }

        for (int i = sb.length() - 1; i >= 0; i--) {
            buf.writeByte((byte) sb.charAt(i));
        }
    }
    public Reply getReply() {
        try {
            countDownLatch.await();
            return reply;
        } catch (InterruptedException e) {
            throw new CommandInterruptedException("interrupted when get replay", e);
        }
    }

    public void setReply(Reply reply) {
        this.reply = reply;
        countDownLatch.countDown();
    }
}
