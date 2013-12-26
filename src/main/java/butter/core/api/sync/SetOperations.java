package butter.core.api.sync;

import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SetOperations<K, V> {

    Set<V> difference(K key, K otherKey);

    Set<V> difference(K key, Collection<K> otherKeys);

    Long differenceAndStore(K key, K otherKey, K destKey);

    Long differenceAndStore(K key, Collection<K> otherKeys, K destKey);

    Set<V> intersect(K key, K otherKey);

    Set<V> intersect(K key, Collection<K> otherKeys);

    Long intersectAndStore(K key, K otherKey, K destKey);

    Long intersectAndStore(K key, Collection<K> otherKeys, K destKey);

    Set<V> union(K key, K otherKey);

    Set<V> union(K key, Collection<K> otherKeys);

    Long unionAndStore(K key, K otherKey, K destKey);

    Long unionAndStore(K key, Collection<K> otherKeys, K destKey);

    Long add(K key, V... values);

    Boolean isMember(K key, Object o);

    Set<V> members(K key);

    Boolean move(K key, V value, K destKey);

    V randomMember(K key);

    Set<V> distinctRandomMembers(K key, long count);

    List<V> randomMembers(K key, long count);

    Long remove(K key, Object... values);

    V pop(K key);

    Long size(K key);

    RedisOperations<K, V> getOperations();
}