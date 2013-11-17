package butter.codec;

import butter.protocol.Command;
import butter.protocol.Reply;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static butter.codec.Attribute.CMD_QUEUE;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午5:22
 */
public class ReplyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(CMD_QUEUE).set(new ConcurrentLinkedQueue<Command>());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Reply reply = (Reply) msg;
        Queue<Command> cmdQueue = ctx.channel().attr(CMD_QUEUE).get();
        cmdQueue.poll().setReply(reply);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
