package com.ig.spring_boot_learning.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ig.spring_boot_learning.base.response.ObjectResponse;
import com.ig.spring_boot_learning.dto.LoginReq;
import com.ig.spring_boot_learning.dto.LoginRsp;
import com.ig.spring_boot_learning.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.ig.spring_boot_learning.utils.AppConstants.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(value = API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/log-in")
    public ObjectResponse<LoginRsp> login(@Valid @RequestBody LoginReq loginReq) throws JsonProcessingException {
        return userService.login(loginReq);
    }
}
