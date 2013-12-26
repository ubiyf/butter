package butter.connection.protocol;

import com.google.common.base.Charsets;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午2:01
 */
public enum Commands {
    //region Keys
    DEL, DUMP, EXISTS, EXPIRE, EXPIREAT, KEYS, MIGRATE, MOVE, OBJECT, PERSIST,
    PEXPIRE, PEXPIREAT, PTTL, RANDOMKEY, RENAME, RENAMENX, RESTORE, SCAN, SORT, TTL,
    TYPE,
    //endregion

    //region Strings
    APPEND, BITCOUNT, BITOP, DECR, DECRBY, GET, GETBIT, GETRANGE, GETSET, INCR,
    INCRBY, INCRBYFLOAT, MGET, MSET, MSETNX, PSETEX, SET, SETBIT, SETEX, SETNX,
    SETRANGE, STRLEN,
    //endregion

    //region Hashes
    HDEL, HEXISTS, HGET, HGETALL, HINCRBY, HINCRBYFLOAT, HKEYS, HLEN, HMGET, HMSET,
    HSCAN, HSET, HSETNX, HVALS,
    //endregion

    //region Lists
    BLPOP, BRPOP, BRPOPLPUSH, LINDEX, LINSERT, LLEN, LPOP, LPUSH, LPUSHX, LRANGE,
    LREM, LSET, LTRIM, RPOP, RPOPLPUSH, RPUSH, RPUSHX,
    //endregion

    //region Sets
    SADD, SCARD, SDIFF, SDIFFSTORE, SINTER, SINTERSTORE, SISMEMBER, SMEMBERS, SMOVE, SPOP,
    SRANDMEMBER, SREM, SUNION, SUNIONSTORE, SSCAN,
    //endregion

    //region Sorted Sets
    ZADD, ZCARD, ZCOUNT, ZINCRBY, ZINTERSTORE, ZRANGE, ZRANGEBYSCORE, ZRANK, ZREM, ZREMRANGEBYRANK,
    ZREMRANGEBYSCORE, ZREVRANGE, ZREVRANGEBYSCORE, ZREVRANK, ZSCAN, ZSCORE, ZUNIONSTORE,
    //endregion

    //region Pub/Sub
    PUBSCRIBE, PUBSUB, PUBLISH, PUNSUBSCRIBE, SUBSCRIBE, UNSUBSCRIBE,
    //endregion

    //region Transactions
    DISCARD, EXEC, MULTI, UNWATCH, WATCH,
    //endregion

    //region Scripting
    EVAL, EVALSHA, SCRIPT,
    //endregion

    //region Connection
    AUTH, ECHO, PING, QUIT, SELECT,
    //endregion

    //region Server
    BGREWRITEAOF, BGSAVE, CLIENT, CONFIG, DBSIZE, DEBUG, FLUSHALL,
    FLUSHDB, INFO, LASTSAVE, MONITOR, SAVE, SHUTDOWN, SLAVEOF, SLOWLOG, SYNC, TIME
    //endregion
    ;
    public final byte[] bytes;

    private Commands() {
        this.bytes = name().getBytes(Charsets.US_ASCII);
    }
}
