package butter.connection;

import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:21
 */
public abstract class Connection {
    protected final Channel channel;

    public Connection(Channel channel) {
        this.channel = channel;
    }

    public void close() {
        channel.close();
    }
}
