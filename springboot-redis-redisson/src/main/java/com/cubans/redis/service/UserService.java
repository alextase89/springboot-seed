package com.cubans.redis.service;

import static java.util.Objects.isNull;

import com.cubans.redis.api.UserRegistrationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final DistributedCacheService distributedCacheService;

    @Autowired
    public UserService(DistributedCacheService distributedCacheService) {
        this.distributedCacheService = distributedCacheService;
    }

    public UserRegistrationRequestDTO registerUser(UserRegistrationRequestDTO user) {
        return this.distributedCacheService.executeDistributedLock(
            user.getUsername(),
            () -> {
                UserRegistrationRequestDTO userOnRedis = this.distributedCacheService.getValue(user.getUsername());
                if (isNull(userOnRedis)) {
                    userOnRedis = this.distributedCacheService.putValue(user.getUsername(), user);
                }
                return userOnRedis;
            }
        );
    }
}
