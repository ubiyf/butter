package butter.core.api.sync;

import org.springframework.data.redis.core.RedisOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ListOperations<K, V> {

    List<V> range(K key, long start, long end);

    void trim(K key, long start, long end);

    Long size(K key);

    Long leftPush(K key, V value);

    Long leftPushAll(K key, V... values);

    Long leftPushIfPresent(K key, V value);

    Long leftPush(K key, V pivot, V value);

    Long rightPush(K key, V value);

    Long rightPushAll(K key, V... values);

    Long rightPushIfPresent(K key, V value);

    Long rightPush(K key, V pivot, V value);

    void set(K key, long index, V value);

    Long remove(K key, long i, Object value);

    V index(K key, long index);

    V leftPop(K key);

    V leftPop(K key, long timeout, TimeUnit unit);

    V rightPop(K key);

    V rightPop(K key, long timeout, TimeUnit unit);

    V rightPopAndLeftPush(K sourceKey, K destinationKey);

    V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit);

    RedisOperations<K, V> getOperations();
}