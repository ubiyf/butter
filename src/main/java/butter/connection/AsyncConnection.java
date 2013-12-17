package butter.connection;

import butter.protocol.BitOPs;
import butter.protocol.Command;
import butter.protocol.Commands;
import butter.protocol.InsertPos;
import io.netty.channel.Channel;

import java.util.List;

import static butter.util.Assert.notEmpty;
import static butter.util.Assert.notNull;
import static butter.util.NumberUtil.doubleToBytes;
import static butter.util.NumberUtil.integerToBytes;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:21
 */
public class AsyncConnection {

    private final Channel channel;

    public AsyncConnection(Channel channel) {
        this.channel = channel;
    }

    public void close() {
        channel.close();
    }

    //region Keys
    public Command<Long> del(byte[]... keys) {
        notNull(keys);

        Command<Long> del = Command.create();
        del.addArg(Commands.DEL.bytes);
        del.addArg(keys);
        channel.writeAndFlush(del);
        return del;
    }

    public Command<byte[]> dump(byte[] key) {
        notNull(key);

        Command<byte[]> dump = Command.create();
        dump.addArg(Commands.DUMP.bytes, key);
        channel.writeAndFlush(dump);
        return dump;
    }

    public Command<Long> exists(byte[] key) {
        notNull(key);

        Command<Long> exists = Command.create();
        exists.addArg(Commands.EXISTS.bytes, key);
        channel.writeAndFlush(exists);
        return exists;
    }

    public Command<Long> expire(byte[] key, int seconds) {
        notNull(key);

        Command<Long> expire = Command.create();
        expire.addArg(Commands.EXPIRE.bytes, key, integerToBytes(seconds));
        channel.writeAndFlush(expire);
        return expire;
    }

    public Command<Long> expireAt(byte[] key, int timestamp) {
        notNull(key);

        Command<Long> expireAt = Command.create();
        expireAt.addArg(Commands.EXPIREAT.bytes, key, integerToBytes(timestamp));
        channel.writeAndFlush(expireAt);
        return expireAt;
    }

    public Command<List<byte[]>> keys(byte[] pattern) {
        notNull(pattern);

        Command<List<byte[]>> keys = Command.create();
        keys.addArg(Commands.KEYS.bytes, pattern);
        channel.writeAndFlush(keys);
        return keys;
    }

    /**
     * MIGRATE needs to perform I/O operations and to honor the specified timeout.
     * When there is an I/O error during the transfer or if the timeout is reached
     * the operation is aborted and the special error - IOERR returned.
     * When this happens the following two cases are possible:
     * The key may be on both the instances.
     * The key may be only in the source instance.
     * It is not possible for the key to get lost in the event of a timeout,
     * but the client calling MIGRATE, in the event of a timeout error,
     * should check if the key is also present in the target instance and act accordingly.
     * <p/>
     * see detail in http://redis.io/commands/migrate
     */
    public Command<String> migrate(byte[] host, int port, int destDB, int timeout, boolean copy, boolean replace) {
        notNull(host);

        Command<String> migrate = Command.create();
        migrate.addArg(Commands.MIGRATE.bytes);
        migrate.addArg(host, integerToBytes(port), integerToBytes(destDB), integerToBytes(timeout));
        if (copy) {
            final byte[] copyOption = {'c', 'o', 'p', 'y'};
            migrate.addArg(copyOption);
        }

        if (replace) {
            final byte[] replaceOption = {'r', 'e', 'p', 'l', 'a', 'c', 'e'};
            migrate.addArg(replaceOption);
        }
        channel.writeAndFlush(migrate);
        return migrate;
    }

    public Command<Long> move(byte[] key, int db) {
        notNull(key);

        Command<Long> move = Command.create();
        move.addArg(Commands.MOVE.bytes, key, integerToBytes(db));
        channel.writeAndFlush(move);
        return move;
    }

    public Command<Long> persist(byte[] key) {
        notNull(key);

        Command<Long> persist = Command.create();
        persist.addArg(Commands.PERSIST.bytes, key);
        channel.writeAndFlush(persist);
        return persist;
    }

    public Command<Long> pexpire(byte[] key, long milliseconds) {
        notNull(key);

        Command<Long> pexpire = Command.create();
        pexpire.addArg(Commands.PEXPIRE.bytes, key, integerToBytes(milliseconds));
        channel.writeAndFlush(pexpire);
        return pexpire;
    }

    public Command<Long> pexpireAt(byte[] key, long milliseconds) {
        notNull(key);

        Command<Long> pexpireAt = Command.create();
        pexpireAt.addArg(Commands.PEXPIREAT.bytes, key, integerToBytes(milliseconds));
        channel.writeAndFlush(pexpireAt);
        return pexpireAt;
    }

    public Command<Long> pttl(byte[] key) {
        notNull(key);

        Command<Long> pttl = Command.create();
        pttl.addArg(Commands.PTTL.bytes, key);
        channel.writeAndFlush(pttl);
        return pttl;
    }

    public Command<byte[]> randomKey() {
        Command<byte[]> randomKey = Command.create();
        randomKey.addArg(Commands.RANDOMKEY.bytes);
        channel.writeAndFlush(randomKey);
        return randomKey;
    }

    public Command<String> rename(byte[] key, byte[] newKey) {
        notNull(key);
        notNull(newKey);

        Command<String> rename = Command.create();
        rename.addArg(Commands.RENAME.bytes, key, newKey);
        channel.writeAndFlush(rename);
        return rename;
    }

    public Command<Long> renameNX(byte[] key, byte[] newKey) {
        notNull(key);
        notNull(newKey);

        Command<Long> renameNX = Command.create();
        renameNX.addArg(Commands.RENAMENX.bytes, key, newKey);
        channel.writeAndFlush(renameNX);
        return renameNX;
    }

    public Command<String> restore(byte[] key, long ttl, byte[] serialized) {
        notNull(key);
        notNull(key);

        Command<String> restore = Command.create();
        restore.addArg(Commands.RESTORE.bytes, key, integerToBytes(ttl), serialized);
        channel.writeAndFlush(restore);
        return restore;
    }

    //TODO unimplemented command scan
    //TODO unimplemented command sort

    public Command<Long> ttl(byte[] key) {
        notNull(key);

        Command<Long> ttl = Command.create();
        ttl.addArg(Commands.TTL.bytes, key);
        channel.writeAndFlush(ttl);
        return ttl;
    }

    public Command<String> type(byte[] key) {
        notNull(key);

        Command<String> type = Command.create();
        type.addArg(Commands.TYPE.bytes, key);
        channel.writeAndFlush(type);
        return type;
    }

    //endregion

    //region Strings
    public Command<Long> append(byte[] key, byte[] value) {
        notNull(key);
        notNull(value);

        Command<Long> append = Command.create();
        append.addArg(Commands.APPEND.bytes, key, value);
        channel.writeAndFlush(append);
        return append;
    }

    public Command<Long> bitCount(byte[] key) {
        notNull(key);

        Command<Long> bitCount = Command.create();
        bitCount.addArg(Commands.BITCOUNT.bytes, key);
        channel.writeAndFlush(bitCount);
        return bitCount;
    }

    public Command<Long> bitCount(byte[] key, int start, int end) {
        notNull(key);

        Command<Long> bitCount = Command.create();
        bitCount.addArg(Commands.BITCOUNT.bytes, key, integerToBytes(start), integerToBytes(end));
        channel.writeAndFlush(bitCount);
        return bitCount;
    }

    public Command<Long> bitOP(BitOPs operation, byte[] destKey, byte[]... key) {
        notNull(destKey);
        notEmpty(key);

        Command<Long> bitOP = Command.create();
        bitOP.addArg(Commands.BITOP.bytes, operation.bytes, destKey);
        bitOP.addArg(key);
        channel.writeAndFlush(bitOP);
        return bitOP;
    }

    public Command<Long> decr(byte[] key) {
        notNull(key);

        Command<Long> decr = Command.create();
        decr.addArg(Commands.DECR.bytes, key);
        channel.writeAndFlush(decr);
        return decr;
    }

    public Command<Long> decrBy(byte[] key, long decrement) {
        notNull(key);

        Command<Long> decrBy = Command.create();
        decrBy.addArg(Commands.DECRBY.bytes, key, integerToBytes(decrement));
        channel.writeAndFlush(decrBy);
        return decrBy;
    }

    public Command<byte[]> get(byte[] key) {
        notNull(key);

        Command<byte[]> get = Command.create();
        get.addArg(Commands.GET.bytes, key);
        channel.writeAndFlush(get);
        return get;
    }

    public Command<Long> getBit(byte[] key, int offset) {
        Command<Long> getBit = Command.create();
        getBit.addArg(Commands.GETBIT.bytes, key, integerToBytes(offset));
        channel.writeAndFlush(getBit);
        return getBit;
    }

    public Command<byte[]> getRange(byte[] key, int start, int end) {
        Command<byte[]> getRange = Command.create();
        getRange.addArg(Commands.GETRANGE.bytes, key, integerToBytes(start), integerToBytes(end));
        channel.writeAndFlush(getRange);
        return getRange;
    }

    public Command<byte[]> getSet(byte[] key, byte[] value) {
        Command<byte[]> getSet = Command.create();
        getSet.addArg(Commands.GETSET.bytes, key, value);
        channel.writeAndFlush(getSet);
        return getSet;
    }

    public Command<Long> incr(byte[] key) {
        Command<Long> incr = Command.create();
        incr.addArg(Commands.INCR.bytes, key);
        channel.writeAndFlush(incr);
        return incr;
    }

    public Command<Long> incrBy(byte[] key, long increment) {
        Command<Long> incrBy = Command.create();
        incrBy.addArg(Commands.INCRBY.bytes, key, integerToBytes(increment));
        channel.writeAndFlush(incrBy);
        return incrBy;
    }

    public Command<byte[]> incrByFloat(byte[] key, double increment) {
        Command<byte[]> incrByFloat = Command.create();
        incrByFloat.addArg(Commands.INCRBYFLOAT.bytes, key, doubleToBytes(increment));
        channel.writeAndFlush(incrByFloat);
        return incrByFloat;
    }

    public Command<List<byte[]>> mget(byte[]... key) {
        Command<List<byte[]>> mget = Command.create();
        mget.addArg(Commands.MGET.bytes);
        mget.addArg(key);
        channel.writeAndFlush(mget);
        return mget;
    }

    public Command<String> mset(byte[]... pair) {
        notEmpty(pair);

        Command<String> mset = Command.create();
        mset.addArg(Commands.MSET.bytes);
        mset.addArg(pair);
        channel.writeAndFlush(mset);
        return mset;
    }

    public Command<Long> msetNX(byte[]... pair) {
        Command<Long> msetNX = Command.create();
        msetNX.addArg(Commands.MSETNX.bytes);
        msetNX.addArg(pair);
        channel.writeAndFlush(msetNX);
        return msetNX;
    }

    public Command<String> psetEX(byte[] key, long milliseconds, byte[] value) {
        Command<String> psetEX = Command.create();
        psetEX.addArg(Commands.PSETEX.bytes, key, integerToBytes(milliseconds), value);
        channel.writeAndFlush(psetEX);
        return psetEX;
    }

    public Command<String> set(byte[] key, byte[] value) {
        notNull(key);
        notNull(value);

        Command<String> set = Command.create();
        set.addArg(Commands.SET.bytes);
        set.addArg(key);
        set.addArg(value);
        channel.writeAndFlush(set);
        return set;
    }

    public Command<Long> setBit(byte[] key, int offset, int value) {
        Command<Long> setBit = Command.create();
        setBit.addArg(Commands.SETBIT.bytes, key, integerToBytes(offset), integerToBytes(value));
        channel.writeAndFlush(setBit);
        return setBit;
    }

    public Command<String> setEX(byte[] key, int seconds, byte[] value) {
        Command<String> setEX = Command.create();
        setEX.addArg(Commands.SETEX.bytes, key, integerToBytes(seconds), value);
        channel.writeAndFlush(setEX);
        return setEX;
    }

    public Command<Long> setNX(byte[] key, byte[] value) {
        Command<Long> setNX = Command.create();
        setNX.addArg(Commands.SETNX.bytes, key, value);
        channel.writeAndFlush(setNX);
        return setNX;
    }

    public Command<Long> setRange(byte[] key, int offset, byte[] value) {
        Command<Long> setRange = Command.create();
        setRange.addArg(Commands.SETRANGE.bytes, key, integerToBytes(offset), value);
        channel.writeAndFlush(setRange);
        return setRange;
    }

    public Command<Long> strLen(byte[] key) {
        Command<Long> strLen = Command.create();
        strLen.addArg(Commands.STRLEN.bytes, key);
        channel.writeAndFlush(strLen);
        return strLen;
    }
    //endregion

    //region Hashes
    public Command<Long> hdel(byte[] key, byte[]... field) {
        Command<Long> hdel = Command.create();
        hdel.addArg(Commands.HDEL.bytes, key);
        hdel.addArg(field);
        channel.writeAndFlush(hdel);
        return hdel;
    }

    public Command<Long> hexists(byte[] key, byte[] field) {
        Command<Long> hexists = Command.create();
        hexists.addArg(Commands.HEXISTS.bytes, key, field);
        channel.writeAndFlush(hexists);
        return hexists;
    }

    public Command<byte[]> hget(byte[] key, byte[] field) {
        Command<byte[]> hget = Command.create();
        hget.addArg(Commands.HGET.bytes, key, field);
        channel.writeAndFlush(hget);
        return hget;
    }

    public Command<List<byte[]>> hgetAll(byte[] key) {
        Command<List<byte[]>> hgetAll = Command.create();
        hgetAll.addArg(Commands.HGETALL.bytes, key);
        channel.writeAndFlush(hgetAll);
        return hgetAll;
    }

    public Command<Long> hincrBy(byte[] key, byte[] field, long increment) {
        Command<Long> hincrBy = Command.create();
        hincrBy.addArg(Commands.HINCRBY.bytes, key, field, integerToBytes(increment));
        channel.writeAndFlush(hincrBy);
        return hincrBy;
    }

    public Command<byte[]> hincrByFloat(byte[] key, byte[] field, double increment) {
        Command<byte[]> hincrByFloat = Command.create();
        hincrByFloat.addArg(Commands.HINCRBYFLOAT.bytes, key, field, doubleToBytes(increment));
        channel.writeAndFlush(hincrByFloat);
        return hincrByFloat;
    }

    public Command<List<byte[]>> hkeys(byte[] key) {
        Command<List<byte[]>> hkeys = Command.create();
        hkeys.addArg(Commands.HKEYS.bytes, key);
        channel.writeAndFlush(hkeys);
        return hkeys;
    }

    public Command<Long> hlen(byte[] key) {
        Command<Long> hlen = Command.create();
        hlen.addArg(Commands.HLEN.bytes, key);
        channel.writeAndFlush(hlen);
        return hlen;
    }

    public Command<List<byte[]>> hmget(byte[] key, byte[]... field) {
        Command<List<byte[]>> hmget = Command.create();
        hmget.addArg(Commands.HMGET.bytes, key);
        hmget.addArg(field);
        channel.writeAndFlush(hmget);
        return hmget;
    }

    public Command<String> hmset(byte[] key, byte[]... pair) {
        Command<String> hmset = Command.create();
        hmset.addArg(Commands.HMSET.bytes, key);
        hmset.addArg(pair);
        channel.writeAndFlush(hmset);
        return hmset;
    }

    public Command<Long> hset(byte[] key, byte[] field, byte[] value) {
        Command<Long> hset = Command.create();
        hset.addArg(Commands.HSET.bytes, key, field, value);
        channel.writeAndFlush(hset);
        return hset;
    }

    public Command<Long> hsetNX(byte[] key, byte[] field, byte[] value) {
        Command<Long> hsetNX = Command.create();
        hsetNX.addArg(Commands.HSETNX.bytes, key, field, value);
        channel.writeAndFlush(hsetNX);
        return hsetNX;
    }

    public Command<List<byte[]>> hvals(byte[] key) {
        Command<List<byte[]>> hvals = Command.create();
        hvals.addArg(Commands.HVALS.bytes, key);
        channel.writeAndFlush(hvals);
        return hvals;
    }
    //endregion

    //region Lists
    public Command<List<byte[]>> blpop(int timeout, byte[]... key) {
        Command<List<byte[]>> blpop = Command.create();
        blpop.addArg(Commands.BLPOP.bytes);
        blpop.addArg(key);
        blpop.addArg(integerToBytes(timeout));
        channel.writeAndFlush(blpop);
        return blpop;
    }

    public Command<List<byte[]>> brpop(int timeout, byte[]... key) {
        Command<List<byte[]>> brpop = Command.create();
        brpop.addArg(Commands.BRPOP.bytes);
        brpop.addArg(key);
        brpop.addArg(integerToBytes(timeout));
        channel.writeAndFlush(brpop);
        return brpop;
    }

    public Command<byte[]> brpopLPush(byte[] source, byte[] destination, int timeout) {
        Command<byte[]> brpopLPush = Command.create();
        brpopLPush.addArg(Commands.BRPOPLPUSH.bytes, source, destination, integerToBytes(timeout));
        channel.writeAndFlush(brpopLPush);
        return brpopLPush;
    }

    public Command<byte[]> lindex(byte[] key, long index) {
        Command<byte[]> lindex = Command.create();
        lindex.addArg(Commands.LINDEX.bytes, key, integerToBytes(index));
        channel.writeAndFlush(lindex);
        return lindex;
    }

    public Command<Long> linsert(byte[] key, InsertPos pos, byte[] pivot, byte[] value) {
        Command<Long> linsert = Command.create();
        linsert.addArg(Commands.LINSERT.bytes, key, pos.bytes, pivot, value);
        channel.writeAndFlush(linsert);
        return linsert;
    }

    public Command<Long> llen(byte[] key) {
        Command<Long> llen = Command.create();
        llen.addArg(Commands.LLEN.bytes, key);
        channel.writeAndFlush(llen);
        return llen;
    }

    public Command<byte[]> lpop(byte[] key) {
        Command<byte[]> lpop = Command.create();
        lpop.addArg(Commands.LPOP.bytes, key);
        channel.writeAndFlush(lpop);
        return lpop;
    }

    public Command<Long> lpush(byte[] key, byte[]... value) {
        notNull(key);
        notEmpty(value);

        Command<Long> lpush = Command.create();
        lpush.addArg(Commands.LPUSH.bytes, key);
        lpush.addArg(value);
        channel.writeAndFlush(lpush);
        return lpush;
    }

    public Command<Long> lpushX(byte[] key, byte[] value) {
        Command<Long> lpushX = Command.create();
        lpushX.addArg(Commands.LPUSHX.bytes, key, value);
        channel.writeAndFlush(lpushX);
        return lpushX;
    }

    public Command<List<byte[]>> lrange(byte[] key, long start, long stop) {
        Command<List<byte[]>> lrange = Command.create();
        lrange.addArg(Commands.LRANGE.bytes, key, integerToBytes(start), integerToBytes(stop));
        channel.writeAndFlush(lrange);
        return lrange;
    }

    public Command<Long> lrem(byte[] key, long count, byte[] value) {
        Command<Long> lrem = Command.create();
        lrem.addArg(Commands.LREM.bytes, key, integerToBytes(count), value);
        channel.writeAndFlush(lrem);
        return lrem;
    }

    public Command<String> lset(byte[] key, long index, byte[] value) {
        Command<String> lset = Command.create();
        lset.addArg(Commands.LSET.bytes, key, integerToBytes(index), value);
        channel.writeAndFlush(lset);
        return lset;
    }

    public Command<String> ltrim(byte[] key, long start, long stop) {
        Command<String> ltrim = Command.create();
        ltrim.addArg(Commands.LTRIM.bytes, key, integerToBytes(start), integerToBytes(stop));
        channel.writeAndFlush(ltrim);
        return ltrim;
    }

    public Command<byte[]> rpop(byte[] key) {
        Command<byte[]> rpop = Command.create();
        rpop.addArg(Commands.RPOP.bytes, key);
        channel.writeAndFlush(rpop);
        return rpop;
    }

    public Command<byte[]> rpopLPush(byte[] source, byte[] destination) {
        Command<byte[]> rpopLPush = Command.create();
        rpopLPush.addArg(Commands.RPOPLPUSH.bytes, source, destination);
        channel.writeAndFlush(rpopLPush);
        return rpopLPush;
    }

    public Command<Long> rpush(byte[] key, byte[]... value) {
        Command<Long> rpush = Command.create();
        rpush.addArg(Commands.RPUSH.bytes, key);
        rpush.addArg(value);
        channel.writeAndFlush(rpush);
        return rpush;
    }

    public Command<Long> rpushX(byte[] key, byte[] value) {
        Command<Long> rpushX = Command.create();
        rpushX.addArg(Commands.RPUSHX.bytes, key, value);
        channel.writeAndFlush(rpushX);
        return rpushX;
    }
    //endregion

    //region Sets
    public Command<Long> sadd(byte[] key, byte[]... member) {
        notNull(key);
        notEmpty(member);

        Command<Long> sadd = Command.create();
        sadd.addArg(Commands.SADD.bytes, key);
        sadd.addArg(member);
        channel.writeAndFlush(sadd);
        return sadd;
    }

    public Command<Long> scard(byte[] key) {
        Command<Long> scard = Command.create();
        scard.addArg(Commands.SCARD.bytes, key);
        channel.writeAndFlush(scard);
        return scard;
    }

    public Command<List<byte[]>> sdiff(byte[]... key) {
        Command<List<byte[]>> sdiff = Command.create();
        sdiff.addArg(Commands.SDIFF.bytes);
        sdiff.addArg(key);
        channel.writeAndFlush(sdiff);
        return sdiff;
    }

    public Command<Long> sdiffStore(byte[] destination, byte[]... key) {
        Command<Long> sdiffStore = Command.create();
        sdiffStore.addArg(Commands.SDIFFSTORE.bytes, destination);
        sdiffStore.addArg(key);
        channel.writeAndFlush(sdiffStore);
        return sdiffStore;
    }

    public Command<List<byte[]>> sinter(byte[]... key) {
        Command<List<byte[]>> sinter = Command.create();
        sinter.addArg(Commands.SINTER.bytes);
        sinter.addArg(key);
        channel.writeAndFlush(sinter);
        return sinter;
    }

    public Command<Long> sinterStore(byte[] destination, byte[]... key) {
        Command<Long> sinterStore = Command.create();
        sinterStore.addArg(Commands.SINTERSTORE.bytes, destination);
        sinterStore.addArg(key);
        channel.writeAndFlush(sinterStore);
        return sinterStore;
    }

    public Command<Long> sisMember(byte[] key, byte[] member) {
        Command<Long> sisMember = Command.create();
        sisMember.addArg(Commands.SISMEMBER.bytes, key, member);
        channel.writeAndFlush(sisMember);
        return sisMember;
    }

    public Command<List<byte[]>> smembers(byte[] key) {
        Command<List<byte[]>> smembers = Command.create();
        smembers.addArg(Commands.SMEMBERS.bytes, key);
        channel.writeAndFlush(smembers);
        return smembers;
    }

    public Command<Long> smove(byte[] source, byte[] destination, byte[] member) {
        Command<Long> smove = Command.create();
        smove.addArg(Commands.SMOVE.bytes, source, destination, member);
        channel.writeAndFlush(smove);
        return smove;
    }

    public Command<byte[]> spop(byte[] key) {
        Command<byte[]> spop = Command.create();
        spop.addArg(Commands.SPOP.bytes, key);
        channel.writeAndFlush(spop);
        return spop;
    }

    public Command<byte[]> srandMember(byte[] key) {
        Command<byte[]> srandMember = Command.create();
        srandMember.addArg(Commands.SRANDMEMBER.bytes, key);
        channel.writeAndFlush(srandMember);
        return srandMember;
    }

    public Command<List<byte[]>> srandMember(byte[] key, long count) {
        Command<List<byte[]>> srandMember = Command.create();
        srandMember.addArg(Commands.SRANDMEMBER.bytes, key, integerToBytes(count));
        channel.writeAndFlush(srandMember);
        return srandMember;
    }

    public Command<Long> srem(byte[] key, byte[]... member) {
        Command<Long> srem = Command.create();
        srem.addArg(Commands.SREM.bytes, key);
        srem.addArg(member);
        channel.writeAndFlush(srem);
        return srem;
    }

    public Command<List<byte[]>> sunion(byte[]... key) {
        Command<List<byte[]>> sunion = Command.create();
        sunion.addArg(Commands.SUNION.bytes);
        sunion.addArg(key);
        channel.writeAndFlush(sunion);
        return sunion;
    }

    public Command<Long> sunionStore(byte[] destination, byte[]... key) {
        Command<Long> sunionStore = Command.create();
        sunionStore.addArg(Commands.SUNIONSTORE.bytes, destination);
        sunionStore.addArg(key);
        channel.writeAndFlush(sunionStore);
        return sunionStore;
    }
    //endregion

    //region Connection
    public Command<String> ping() {
        Command<String> ping = Command.create();
        ping.addArg(Commands.PING.bytes);
        channel.writeAndFlush(ping);
        return ping;
    }

    public Command<String> select(int index) {
        Command<String> select = Command.create();
        select.addArg(Commands.SELECT.bytes, integerToBytes(index));
        channel.writeAndFlush(select);
        return select;
    }
    //endregion

    //region Server
    public Command<String> flushDB() {
        Command<String> flushDB = Command.create();
        flushDB.addArg(Commands.FLUSHDB.bytes);
        channel.writeAndFlush(flushDB);
        return flushDB;
    }

    public Command<String> flushAll() {
        Command<String> flushAll = Command.create();
        flushAll.addArg(Commands.FLUSHALL.bytes);
        channel.writeAndFlush(flushAll);
        return flushAll;
    }
    //endregion
}
