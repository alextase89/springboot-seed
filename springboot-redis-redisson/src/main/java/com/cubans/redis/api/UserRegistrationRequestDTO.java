package com.cubans.redis.api;

import lombok.Data;

@Data
public class UserRegistrationRequestDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
