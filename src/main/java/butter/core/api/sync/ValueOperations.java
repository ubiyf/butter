package butter.core.api.sync;

import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface ValueOperations<K, V> {

    void set(K key, V value);

    void set(K key, V value, long timeout, TimeUnit unit);

    Boolean setIfAbsent(K key, V value);

    void multiSet(Map<? extends K, ? extends V> m);

    Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m);

    V get(Object key);

    V getAndSet(K key, V value);

    List<V> multiGet(Collection<K> keys);

    Long increment(K key, long delta);

    Double increment(K key, double delta);

    Integer append(K key, String value);

    String get(K key, long start, long end);

    void set(K key, V value, long offset);

    Long size(K key);

    RedisOperations<K, V> getOperations();
}