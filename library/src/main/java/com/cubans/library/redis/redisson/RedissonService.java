package com.cubans.library.redis.redisson;

import com.cubans.library.redis.redisson.map.RedissonMap;
import java.util.HashMap;
import java.util.Map;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedissonService {

    private Map<String, RedissonMap> mapsConfig;
    private final RedissonClient redissonClient;

    @Autowired
    public RedissonService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.mapsConfig = new HashMap<>();
    }

    public <V> RedissonMap<V> getMap(String key, Class<V> vClass) {
        if(!mapsConfig.containsKey(key)){
            RedissonMap<V> redissonMap = new RedissonMap<>(this.redissonClient, vClass, key);
            this.mapsConfig.put(key, redissonMap);
        }
        return this.mapsConfig.get(key);
    }
}
