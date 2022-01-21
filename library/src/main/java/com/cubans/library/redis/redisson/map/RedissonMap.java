package com.cubans.library.redis.redisson.map;

import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import com.cubans.library.redis.redisson.util.SerializerUtil;

@Slf4j
public class RedissonMap<V> {

    @Getter
    private final RMapCache<String, String> redisMap;
    private final Class<V> classType;
    @Setter
    private long ttl;

    public RedissonMap(RedissonClient client, Class<V> classType, String redisMapName) {
        this.redisMap = client.getMapCache(redisMapName);
        this.classType = classType;
    }

    public V put(String key, V value) {
        String serializedValue = SerializerUtil.serialize(value);
        V valueOnRedis = null;
        try {
            serializedValue = this.redisMap.put(key, serializedValue, this.ttl, TimeUnit.SECONDS);
            valueOnRedis = SerializerUtil.deserialize(serializedValue, this.classType);
        } catch (Exception ex) {
            log.error("Putting error {}", ex.getMessage(), ex);
        }
        return valueOnRedis;
    }

    public V get(String key) {
        log.debug("Getting key: {}", key);
        V valueOnRedis = null;

        try {
            valueOnRedis = SerializerUtil.deserialize(this.redisMap.get(key), this.classType);
        } catch (Exception ex) {
            log.error("Getting from Redis server error", ex);
        }
        return valueOnRedis;
    }

    @SuppressWarnings("unchecked")
    public void remove(String key) {
        try {
            this.redisMap.fastRemove(key);
        } catch (Exception ex) {
            log.error("Evict error", ex);
        }
    }

    public void lock(String key, long timeout, TimeUnit unit) {
        RLock lock = this.redisMap.getLock(key);
        lock.lock(timeout, unit);
    }

    public void unlock(String key) {
        RLock lock = this.redisMap.getLock(key);
        lock.unlock();
    }
}
