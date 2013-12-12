package butter.connection;

import io.netty.channel.Channel;

import java.util.List;

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

    public void close() {
        async.close();
    }

    //region Keys
    public long del(byte[]... keys) {
        return async.del(keys).get();
    }

    public byte[] dump(byte[] key) {
        return async.dump(key).get();
    }

    public long exists(byte[] key) {
        return async.exists(key).get();
    }

    public long expire(byte[] key, int seconds) {
        return async.expire(key, seconds).get();
    }

    public long expireAt(byte[] key, int timestamp) {
        return async.expireAt(key, timestamp).get();
    }

    public List<byte[]> keys(byte[] pattern) {
        return async.keys(pattern).get();
    }

    public void migrate(byte[] host, int port, int destDB, int timeout, boolean copy, boolean replace) {
        async.migrate(host, port, destDB, timeout, copy, replace).get();
    }

    public long move(byte[] key, int db) {
        return async.move(key, db).get();
    }

    public void restore(byte[] key, long ttl, byte[] serialized) {
        async.restore(key, ttl, serialized).get();
    }

    public long ttl(byte[] key) {
        return async.ttl(key).get();
    }
    //endregion

    //region Strings
    public void set(byte[] key, byte[] value) {
        async.set(key, value).get();
    }

    public String mset(byte[]... pair) {
        return async.mset(pair).get();
    }

    public byte[] get(byte[] key) {
        return async.get(key).get();
    }
    //endregion

    //region connection
    public void ping() {
        async.ping().get();
    }

    public void select(int index) {
        async.select(index).get();
    }
    //endregion

    //region Server
    public void flushDB() {
        async.flushDB().get();
    }

    public void flushAll() {
        async.flushAll().get();
    }
    //endregion
}
