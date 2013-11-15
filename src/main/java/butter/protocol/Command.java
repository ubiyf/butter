package butter.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:17
 */
public class Command {
    private static final byte[] CRLF = "\r\n".getBytes(Charset.forName("US-ASCII"));

    private List<byte[]> args = new ArrayList<byte[]>();

    public void addArg(byte[] arg) {
        args.add(arg);
    }

    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeChar('*');
        buf.writeChar(args.size());
        buf.writeBytes(CRLF);
        for (byte[] arg : args) {
            buf.writeChar('$');
            buf.writeChar(arg.length);
            buf.writeBytes(CRLF);
            buf.writeBytes(arg);
            buf.writeBytes(CRLF);
        }

        return data;
    }
}
