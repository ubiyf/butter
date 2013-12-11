package butter.connection;

import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午2:15
 */
public class SyncConnection {
    private final AsyncConnection async;

    public SyncConnection(Channel channel) {
        this.async = new AsyncConnection(channel);
    }

    //region Keys
    public long del(byte[]... keys) {
        return async.del(keys).get();
    }

    public byte[] dump(byte[] key) {
        return async.dump(key).get();
    }

    public void restore(byte[] key, long ttl, byte[] serialized) {
        async.restore(key, ttl, serialized);
    }
    //endregion

    //region Strings
    public void set(byte[] key, byte[] value) {
        async.set(key, value).get();
    }

    public byte[] get(byte[] key) {
        return async.get(key).get();
    }
    //endregion

    //region connection
    public void ping() {
        async.ping().get();
    }
    //endregion
}
