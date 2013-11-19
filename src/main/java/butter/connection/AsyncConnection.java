package butter.connection;

import butter.protocol.Command;
import butter.protocol.Commands;
import butter.protocol.replies.StatusReply;
import io.netty.channel.Channel;

import java.util.concurrent.Future;

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

    public Future<StatusReply> set(byte[] key, byte[] value) {
        Command<StatusReply> set = new Command<StatusReply>();
        set.addArg(Commands.SET.bytes);
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        return set;
    }
}
