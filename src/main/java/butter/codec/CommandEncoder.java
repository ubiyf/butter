package butter.codec;

import butter.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Queue;

import static butter.codec.Attribute.CMD_QUEUE;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:14
 */
public class CommandEncoder extends MessageToByteEncoder<Command> {
    private ByteBuf buf = Unpooled.buffer();

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, ByteBuf out) throws Exception {
        Queue<Command> cmdQueue = ctx.channel().attr(CMD_QUEUE).get();
        cmdQueue.offer(command);
        command.encode(buf);
        out.writeBytes(buf);
    }
}
