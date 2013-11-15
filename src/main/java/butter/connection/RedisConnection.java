package butter.connection;

import butter.protocol.Command;
import io.netty.channel.Channel;

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

        channel.write(set);


    }
}
