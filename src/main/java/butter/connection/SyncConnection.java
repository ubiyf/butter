package butter.connection;

import butter.protocol.BitOPs;
import butter.protocol.InsertPos;
import butter.util.Pair;
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
    public List<byte[]> blpop(int timeout, byte[]... key) {
        return async.blpop(timeout, key).get();
    }

    public List<byte[]> brpop(int timeout, byte[]... key) {
        return async.brpop(timeout, key).get();
    }

    public byte[] brpopLPush(byte[] source, byte[] destination, int timeout) {
        return async.brpopLPush(source, destination, timeout).get();
    }

    public byte[] lindex(byte[] key, long index) {
        return async.lindex(key, index).get();
    }

    public long linsert(byte[] key, InsertPos pos, byte[] pivot, byte[] value) {
        return async.linsert(key, pos, pivot, value).get();
    }

    public long llen(byte[] key) {
        return async.llen(key).get();
    }

    public byte[] lpop(byte[] key) {
        return async.lpop(key).get();
    }

    public long lpush(byte[] key, byte[]... value) {
        return async.lpush(key, value).get();
    }

    public long lpushX(byte[] key, byte[] value) {
        return async.lpushX(key, value).get();
    }

    public List<byte[]> lrange(byte[] key, long start, long stop) {
        return async.lrange(key, start, stop).get();
    }

    public long lrem(byte[] key, long count, byte[] value) {
        return async.lrem(key, count, value).get();
    }

    public String lset(byte[] key, long index, byte[] value) {
        return async.lset(key, index, value).get();
    }

    public String ltrim(byte[] key, long start, long stop) {
        return async.ltrim(key, start, stop).get();
    }

    public byte[] rpop(byte[] key) {
        return async.rpop(key).get();
    }

    public byte[] rpopLPush(byte[] source, byte[] destination) {
        return async.rpopLPush(source, destination).get();
    }

    public long rpush(byte[] key, byte[]... value) {
        return async.rpush(key, value).get();
    }

    public long rpushX(byte[] key, byte[] value) {
        return async.rpushX(key, value).get();
    }
    //endregion

    //region sets
    public long sadd(byte[] key, byte[]... member) {
        return async.sadd(key, member).get();
    }

    public long scard(byte[] key) {
        return async.scard(key).get();
    }

    public List<byte[]> sdiff(byte[]... key) {
        return async.sdiff(key).get();
    }

    public long sdiffStore(byte[] destination, byte[]... key) {
        return async.sdiffStore(destination, key).get();
    }

    public List<byte[]> sinter(byte[]... key) {
        return async.sinter(key).get();
    }

    public long sinterStore(byte[] destination, byte[]... key) {
        return async.sinterStore(destination, key).get();
    }

    public long sisMember(byte[] key, byte[] member) {
        return async.sisMember(key, member).get();
    }

    public List<byte[]> smembers(byte[] key) {
        return async.smembers(key).get();
    }

    public long smove(byte[] source, byte[] destination, byte[] member) {
        return async.smove(source, destination, member).get();
    }

    public byte[] spop(byte[] key) {
        return async.spop(key).get();
    }

    public byte[] srandMember(byte[] key) {
        return async.srandMember(key).get();
    }

    public List<byte[]> srandMember(byte[] key, long count) {
        return async.srandMember(key, count).get();
    }

    public long srem(byte[] key, byte[]... member) {
        return async.srem(key, member).get();
    }

    public List<byte[]> sunion(byte[]... key) {
        return async.sunion(key).get();
    }

    public long sunionStore(byte[] destination, byte[]... key) {
        return async.sunionStore(destination, key).get();
    }
    //endregion

    //region Sorted Sets
    public long zadd(byte[] key, Pair<Double, byte[]>... pair) {
        return async.zadd(key, pair).get();
    }

    public long zcard(byte[] key) {
        return async.zcard(key).get();
    }

    public long zcount(byte[] key, byte[] min, byte[] max) {
        return async.zcount(key, min, max).get();
    }

    public byte[] zincrBy(byte[] key, long increment, byte[] member) {
        return async.zincrBy(key, increment, member).get();
    }

    public List<byte[]> zrange(byte[] key, long start, long stop, boolean withScores) {
        return async.zrange(key, start, stop, withScores).get();
    }

    public List<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, boolean withScores, long offset, long count) {
        return async.zrangeByScore(key, min, max, withScores, offset, count).get();
    }

    /**
     * @return null will be returned If member
     * does not exist in the sorted set or key does not exist
     */
    public Long zrank(byte[] key, byte[] member) {
        return async.zrank(key, member).get();
    }

    public long zrem(byte[] key, byte[]... member) {
        return async.zrem(key, member).get();
    }

    public long zremRangeByRank(byte[] key, long start, long stop) {
        return async.zremRangeByRank(key, start, stop).get();
    }

    public long zremRangeByScore(byte[] key, byte[] min, byte[] max) {
        return async.zremRangeByScore(key, min, max).get();
    }

    public List<byte[]> zrevRange(byte[] key, long start, long stop, boolean withScores) {
        return async.zrevRange(key, start, stop, withScores).get();
    }

    public List<byte[]> zrevRangeByScore(byte[] key, byte[] max, byte[] min, boolean withScores, long offset, long count) {
        return async.zrevRangeByScore(key, max, min, withScores, offset, count).get();
    }

    public Long zrevRank(byte[] key, byte[] member) {
        return async.zrevRank(key, member).get();
    }

    public byte[] zscore(byte[] key, byte[] member) {
        return async.zscore(key, member).get();
    }
    //endregion

    //region transactions
    public void discard() {
        async.discard().get();
    }

    public List<byte[]> exec() {
        return async.exec().get();
    }

    public void multi() {
        async.multi().get();
    }

    public void unwatch() {
        async.unwatch().get();
    }

    public void watch(byte[]... key) {
        async.watch(key).get();
    }
    //endregion

    //region connection
    public void auth(byte[] passwrod) {
        async.auth(passwrod).get();
    }

    public byte[] echo(byte[] message) {
        return async.echo(message).get();
    }

    public void ping() {
        async.ping().get();
    }

    public void quit() {
        async.quit().get();
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
