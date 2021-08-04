package com.cubans.awssqs.api;

import lombok.Data;

@Data

public class UserRegistrationRequestDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRolEnum Rol;
}
