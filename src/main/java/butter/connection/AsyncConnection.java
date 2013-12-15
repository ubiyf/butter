package butter.connection;

import butter.protocol.BitOPs;
import butter.protocol.Command;
import butter.protocol.Commands;
import io.netty.channel.Channel;

import java.util.List;

import static butter.util.Assert.notEmpty;
import static butter.util.Assert.notNull;
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

    public Command<byte[]> incrByFloat(byte[] key, float increment) {
        Command<byte[]> incrByFloat = Command.create();
        incrByFloat.addArg(Commands.INCRBYFLOAT.bytes);
        channel.writeAndFlush(incrByFloat);
        return incrByFloat;
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


    public Command<String> mset(byte[]... pair) {
        notEmpty(pair);

        Command<String> mset = Command.create();
        mset.addArg(Commands.MSET.bytes);
        mset.addArg(pair);
        channel.writeAndFlush(mset);
        return mset;
    }
    //endregion

    //region Lists
    public Command<Long> lpush(byte[] key, byte[]... value) {
        notNull(key);
        notEmpty(value);

        Command<Long> lpush = Command.create();
        lpush.addArg(Commands.LPUSH.bytes, key);
        lpush.addArg(value);
        channel.writeAndFlush(lpush);
        return lpush;
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
