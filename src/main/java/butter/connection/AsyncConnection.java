package butter.connection;

import butter.protocol.Command;
import butter.protocol.Commands;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:21
 */
public class AsyncConnection {

    private final Channel channel;

    public void close() {
        channel.close();
    }

    public AsyncConnection(Channel channel) {
        this.channel = channel;
    }

    //region Keys
    public Command<Long> del(byte[]... keys) {
        Command<Long> del = Command.create(Long.class);
        del.addArg(Commands.DEL.bytes);
        del.addArg(keys);
        channel.writeAndFlush(del);
        return del;
    }

    public Command<byte[]> dump(byte[] key) {
        Command<byte[]> dump = Command.create(byte[].class);
        dump.addArg(Commands.DUMP.bytes, key);
        channel.writeAndFlush(dump);
        return dump;
    }

    public Command<String> restore(byte[] key, long ttl, byte[] serialized) {
        Command<String> restore = Command.create(String.class);
        restore.addArg(Commands.RESTORE.bytes, key, Long.toString(ttl).getBytes(), serialized);
        channel.writeAndFlush(restore);
        return restore;
    }
    //endregion

    //region Strings
    public Command<byte[]> get(byte[] key) {
        Command<byte[]> get = Command.create(byte[].class);
        get.addArg(Commands.GET.bytes, key);
        channel.writeAndFlush(get);
        return get;
    }

    public Command<String> set(byte[] key, byte[] value) {
        Command<String> set = Command.create(String.class);
        set.addArg(Commands.SET.bytes);
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        return set;
    }
    //endregion

    public Command<String> ping() {
        Command<String> ping = Command.create(String.class);
        ping.addArg(Commands.PING.bytes);
        channel.writeAndFlush(ping);
        return ping;
    }


}
