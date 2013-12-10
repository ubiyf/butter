package butter.connection;

import butter.protocol.Command;
import butter.protocol.Commands;
import butter.protocol.replies.StatusReply;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:21
 */
public class AsyncConnection extends Connection {

    public AsyncConnection(Channel channel) {
        super(channel);
    }

    public ListenableFuture<StatusReply> ping() {
        Command<StatusReply> ping = Command.create(StatusReply.class);
        ping.addArg(Commands.PING.bytes);
        channel.writeAndFlush(ping);
        return ping;
    }

    public ListenableFuture<StatusReply> set(byte[] key, byte[] value) {
        Command<StatusReply> set = Command.create(StatusReply.class);
        set.addArg(Commands.SET.bytes);
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        return set;
    }
}
