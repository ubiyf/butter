package butter.protocol;

import butter.protocol.replies.StatusReply;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午6:45
 */
public class CommandTest {

    @Test
    public void testEncode() {
        Command<StatusReply> set = Command.create();
        set.addArg("SET".getBytes());
        set.addArg("mykey".getBytes());
        set.addArg("myvalue".getBytes());
        ByteBuf buf = Unpooled.buffer();
        set.encode(buf);
    }
}
