package butter.connection;

import butter.exception.RedisException;
import butter.protocol.Command;
import butter.protocol.Commands;
import butter.protocol.replies.StatusReply;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午2:15
 */
public class SyncConnection extends Connection {

    public SyncConnection(Channel channel) {
        super(channel);
    }

    public void ping() {
        Command<StatusReply> ping = Command.create(StatusReply.class);
        ping.addArg(Commands.PING.bytes);
        channel.writeAndFlush(ping);
        StatusReply status = ping.get();
        //TODO 等待ReplyDecoder重构完成
    }

    public void set(byte[] key, byte[] value) {
        Command<StatusReply> set = Command.create(StatusReply.class);
        set.addArg(Commands.SET.bytes);
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        StatusReply status = set.get();
        if (!status.getStatus().equals("OK")) {
            throw new RedisException("set error");
        }
    }
}
