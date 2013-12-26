package butter.core.api.sync;

import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.Set;

public interface ZSetOperations<K, V> {

    /**
     * Typed ZSet tuple.
     */
    public interface TypedTuple<V> extends Comparable<Double> {
        V getValue();

        Double getScore();
    }

    Long intersectAndStore(K key, K otherKey, K destKey);

    Long intersectAndStore(K key, Collection<K> otherKeys, K destKey);

    Long unionAndStore(K key, K otherKey, K destKey);

    Long unionAndStore(K key, Collection<K> otherKeys, K destKey);

    Set<V> range(K key, long start, long end);

    Set<V> reverseRange(K key, long start, long end);

    Set<TypedTuple<V>> rangeWithScores(K key, long start, long end);

    Set<TypedTuple<V>> reverseRangeWithScores(K key, long start, long end);

    Set<V> rangeByScore(K key, double min, double max);

    Set<V> rangeByScore(K key, double min, double max, long offset, long count);

    Set<V> reverseRangeByScore(K key, double min, double max);

    Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count);

    Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max);

    Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max, long offset, long count);

    Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max);

    Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max, long offset, long count);

    Boolean add(K key, V value, double score);

    Long add(K key, Set<TypedTuple<V>> tuples);

    Double incrementScore(K key, V value, double delta);

    Long rank(K key, Object o);

    Long reverseRank(K key, Object o);

    Double score(K key, Object o);

    Long remove(K key, Object... values);

    Long removeRange(K key, long start, long end);

    Long removeRangeByScore(K key, double min, double max);

    Long count(K key, double min, double max);

    Long size(K key);

    RedisOperations<K, V> getOperations();
}