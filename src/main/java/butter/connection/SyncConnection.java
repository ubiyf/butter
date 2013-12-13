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

    public long persist(byte[] key) {
        return async.persist(key).get();
    }

    public long pexpire(byte[] key, long milliseconds) {
        return async.pexpire(key, milliseconds).get();
    }

    public long pexpireAt(byte[] key, long milliseconds) {
        return async.pexpireAt(key, milliseconds).get();
    }

    public long pttl(byte[] key) {
        return async.pttl(key).get();
    }

    public byte[] randomKey() {
        return async.randomKey().get();
    }

    public void rename(byte[] key, byte[] newKey) {
        async.rename(key, newKey).get();
    }

    public long renameNX(byte[] key, byte[] newKey) {
        return async.renameNX(key, newKey).get();
    }

    public void restore(byte[] key, long ttl, byte[] serialized) {
        async.restore(key, ttl, serialized).get();
    }

    public long ttl(byte[] key) {
        return async.ttl(key).get();
    }

    public String type(byte[] key) {
        return async.type(key).get();
    }

    //endregion

    //region Strings
    public long append(byte[] key, byte[] value) {
        return async.append(key, value).get();
    }

    public long bitCount(byte[] key) {
        return async.bitCount(key).get();
    }

    public long bitCount(byte[] key, int start, int end) {
        return async.bitCount(key, start, end).get();
    }


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

    //region Lists
    public Long lpush(byte[] key, byte[] value) {
        return async.lpush(key, value).get();
    }
    //endregion

    public Long sadd(byte[] key, byte[]... member) {
        return async.sadd(key, member).get();
    }

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
