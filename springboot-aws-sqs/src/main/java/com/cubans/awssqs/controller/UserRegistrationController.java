package com.cubans.awssqs.controller;

import com.cubans.awssqs.api.UserRegistrationRequestDTO;
import com.cubans.awssqs.api.UserRegistrationResponseDTO;
import com.cubans.awssqs.configuration.Constant;
import com.cubans.awssqs.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constant.USER_SERVICE)
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserRegistrationResponseDTO registerUser(
        @Parameter(name = "User Request Body") @RequestBody UserRegistrationRequestDTO userRegistrationRequest) {

        log.info("Receive new request for registerUser {}", userRegistrationRequest);
        return this.userService.registerUser(userRegistrationRequest);
    }
}
