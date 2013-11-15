package butter.codec;

import butter.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:14
 */
public class CommandEncoder extends MessageToByteEncoder<Command> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf out) throws Exception {
        out.writeBytes(command.encode());
    }
}
