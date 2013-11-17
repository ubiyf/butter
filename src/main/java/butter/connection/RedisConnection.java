package butter.connection;

import butter.exception.RedisException;
import butter.protocol.Command;
import butter.protocol.Reply;
import butter.protocol.replies.StatusReply;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午2:15
 */
public class RedisConnection {
    private final Channel channel;

    public RedisConnection(Channel channel) {
        this.channel = channel;
    }

    public void set(byte[] key, byte[] value) {
        Command set = new Command();
        set.addArg("SET".getBytes());
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        StatusReply status = (StatusReply) set.getReply();
        if (!status.getStatus().equals("OK")) {
            throw new RedisException("set error");
        }
    }
}
