package butter.connection;

import butter.protocol.BitOPs;
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

    public long bitOP(BitOPs operation, byte[] destKey, byte[]... key) {
        return async.bitOP(operation, destKey, key).get();
    }

    public long decr(byte[] key) {
        return async.decr(key).get();
    }

    public long decrBy(byte[] key, long decrement) {
        return async.decrBy(key, decrement).get();
    }

    public byte[] get(byte[] key) {
        return async.get(key).get();
    }

    public long getBit(byte[] key, int offset) {
        return async.getBit(key, offset).get();
    }

    public byte[] getRange(byte[] key, int start, int end) {
        return async.getRange(key, start, end).get();
    }

    public byte[] getSet(byte[] key, byte[] value) {
        return async.getSet(key, value).get();
    }

    public long incr(byte[] key) {
        return async.incr(key).get();
    }

    public long incrBy(byte[] key, long increment) {
        return async.incrBy(key, increment).get();
    }

    public byte[] incrByFloat(byte[] key, double increment) {
        return async.incrByFloat(key, increment).get();
    }

    public List<byte[]> mget(byte[]... key) {
        return async.mget(key).get();
    }

    public long msetNX(byte[]... pair) {
        return async.msetNX(pair).get();
    }

    public String psetEX(byte[] key, long milliseconds, byte[] value) {
        return async.psetEX(key, milliseconds, value).get();
    }

    public void set(byte[] key, byte[] value) {
        async.set(key, value).get();
    }

    public long setBit(byte[] key, int offset, int value) {
        return async.setBit(key, offset, value).get();
    }

    public String setEX(byte[] key, int seconds, byte[] value) {
        return async.setEX(key, seconds, value).get();
    }

    public String mset(byte[]... pair) {
        return async.mset(pair).get();
    }

    public long setNX(byte[] key, byte[] value) {
        return async.setNX(key, value).get();
    }

    public long setRange(byte[] key, int offset, byte[] value) {
        return async.setRange(key, offset, value).get();
    }

    public long strLen(byte[] key) {
        return async.strLen(key).get();
    }
    //endregion

    //region Hashes
    public long hdel(byte[] key, byte[]... field) {
        return async.hdel(key, field).get();
    }

    public long hexists(byte[] key, byte[] value) {
        return async.hexists(key, value).get();
    }

    public byte[] hget(byte[] key, byte[] field) {
        return async.hget(key, field).get();
    }

    public List<byte[]> hgetAll(byte[] key) {
        return async.hgetAll(key).get();
    }

    public long hincrBy(byte[] key, byte[] field, long increment) {
        return async.hincrBy(key, field, increment).get();
    }

    public byte[] hincrByFloat(byte[] key, byte[] field, double increment) {
        return async.hincrByFloat(key, field, increment).get();
    }

    public List<byte[]> hkeys(byte[] key) {
        return async.hkeys(key).get();
    }

    public long hlen(byte[] key) {
        return async.hlen(key).get();
    }

    public List<byte[]> hmget(byte[] key, byte[]... field) {
        return async.hmget(key, field).get();
    }

    public String hmset(byte[] key, byte[]... field) {
        return async.hmset(key, field).get();
    }

    public long hset(byte[] key, byte[] field, byte[] value) {
        return async.hset(key, field, value).get();
    }

    public long hsetNX(byte[] key, byte[] field, byte[] value) {
        return async.hsetNX(key, field, value).get();
    }

    public List<byte[]> hvals(byte[] key) {
        return async.hvals(key).get();
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
