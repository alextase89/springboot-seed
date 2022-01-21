package com.cubans.redis.service;

import com.cubans.library.redis.redisson.map.RedissonMap;
import com.cubans.library.redis.redisson.RedissonService;
import com.cubans.redis.api.UserRegistrationRequestDTO;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DistributedCacheService {

    public static final String USER_CACHE_MAP_KEY = "user_cache_map";
    public static final String DISTRIBUTED_LOCK_MAP_KEY = "distributed_lock_map";

    private RedissonMap<String> distributedLockMap;
    private RedissonMap<UserRegistrationRequestDTO> userRedisMap;

    @Value("${redisson.lock.lease-ttl:180}")
    private Long leaseTtl;

    private final RedissonService redissonService;

    @Autowired
    public DistributedCacheService(RedissonService redissonService) {
        this.redissonService = redissonService;
    }

    @PostConstruct
    private void init() {
        this.distributedLockMap = this.redissonService.getMap(DISTRIBUTED_LOCK_MAP_KEY, String.class);
        this.userRedisMap = this.redissonService.getMap(USER_CACHE_MAP_KEY, UserRegistrationRequestDTO.class);
    }

    public UserRegistrationRequestDTO getValue(String key) {
        try {
            return userRedisMap.get(key);
        } catch (Exception ex) {
            log.error("Error get value from cache: {}", ex.getMessage(), ex);
        }
        return null;
    }

    public UserRegistrationRequestDTO putValue(String key, UserRegistrationRequestDTO user) {
        try {
            return userRedisMap.put(key, user);
        } catch (Exception ex) {
            log.error("Error get value from cache: {}", ex.getMessage(), ex);
        }
        return null;
    }

    public <R> R executeDistributedLock(String lockKey, Supplier<R> supp) {
        this.distributedLockMap.lock(lockKey, leaseTtl, TimeUnit.SECONDS);
        try {
            return supp.get();
        } finally {
            this.distributedLockMap.unlock(lockKey);
        }
    }
}
