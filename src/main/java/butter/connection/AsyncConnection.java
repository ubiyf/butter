package butter.connection;

import butter.protocol.Command;
import butter.protocol.Commands;
import io.netty.channel.Channel;

import java.util.List;

import static butter.util.NumberUtil.integerToBytes;
import static butter.util.TypeReference.LIST_BYTES;

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

    public Command<Long> exists(byte[] key) {
        Command<Long> exists = Command.create(Long.class);
        exists.addArg(Commands.EXISTS.bytes, key);
        channel.writeAndFlush(exists);
        return exists;
    }

    public Command<Long> expire(byte[] key, int seconds) {
        Command<Long> expire = Command.create(Long.class);
        expire.addArg(Commands.EXPIRE.bytes, key, integerToBytes(seconds));
        channel.writeAndFlush(expire);
        return expire;
    }

    public Command<Long> expireAt(byte[] key, int timestamp) {
        Command<Long> expireAt = Command.create(Long.class);
        expireAt.addArg(Commands.EXPIREAT.bytes, key, integerToBytes(timestamp));
        channel.writeAndFlush(expireAt);
        return expireAt;
    }

    public Command<List<byte[]>> keys(byte[] pattern) {
        Command<List<byte[]>> keys = Command.create(LIST_BYTES);
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
        Command<String> migrate = Command.create(String.class);
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
        Command<Long> move = Command.create(Long.class);
        move.addArg(Commands.MOVE.bytes, key, integerToBytes(db));
        channel.writeAndFlush(move);
        return move;
    }

    public Command<String> restore(byte[] key, long ttl, byte[] serialized) {
        Command<String> restore = Command.create(String.class);
        restore.addArg(Commands.RESTORE.bytes, key, integerToBytes(ttl), serialized);
        channel.writeAndFlush(restore);
        return restore;
    }

    public Command<Long> ttl(byte[] key) {
        Command<Long> ttl = Command.create(Long.class);
        ttl.addArg(Commands.TTL.bytes, key);
        channel.writeAndFlush(ttl);
        return ttl;
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

    public Command<String> mset(byte[]... pair) {
        Command<String> mset = Command.create(String.class);
        mset.addArg(Commands.MSET.bytes);
        mset.addArg(pair);
        channel.writeAndFlush(mset);
        return mset;
    }
    //endregion

    //region Connection
    public Command<String> ping() {
        Command<String> ping = Command.create(String.class);
        ping.addArg(Commands.PING.bytes);
        channel.writeAndFlush(ping);
        return ping;
    }

    public Command<String> select(int index) {
        Command<String> select = Command.create(String.class);
        select.addArg(Commands.SELECT.bytes, integerToBytes(index));
        channel.writeAndFlush(select);
        return select;
    }
    //endregion

    //region Server
    public Command<String> flushDB() {
        Command<String> flushDB = Command.create(String.class);
        flushDB.addArg(Commands.FLUSHDB.bytes);
        channel.writeAndFlush(flushDB);
        return flushDB;
    }

    public Command<String> flushAll() {
        Command<String> flushAll = Command.create(String.class);
        flushAll.addArg(Commands.FLUSHALL.bytes);
        channel.writeAndFlush(flushAll);
        return flushAll;
    }
    //endregion
}
