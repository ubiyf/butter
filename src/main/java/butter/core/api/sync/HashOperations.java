package butter.core.api.sync;

import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HashOperations<H, HK, HV> {

    void delete(H key, Object... hashKeys);

    Boolean hasKey(H key, Object hashKey);

    HV get(H key, Object hashKey);

    List<HV> multiGet(H key, Collection<HK> hashKeys);

    Long increment(H key, HK hashKey, long delta);

    Double increment(H key, HK hashKey, double delta);

    Set<HK> keys(H key);

    Long size(H key);

    void putAll(H key, Map<? extends HK, ? extends HV> m);

    void put(H key, HK hashKey, HV value);

    Boolean putIfAbsent(H key, HK hashKey, HV value);

    List<HV> values(H key);

    Map<HK, HV> entries(H key);

    RedisOperations<H, ?> getOperations();
}
