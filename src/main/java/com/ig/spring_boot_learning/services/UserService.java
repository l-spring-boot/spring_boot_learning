package com.ig.spring_boot_learning.services;

import com.ig.spring_boot_learning.base.response.MessageResponse;
import com.ig.spring_boot_learning.base.response.ObjectResponse;
import com.ig.spring_boot_learning.dto.LoginReq;
import com.ig.spring_boot_learning.dto.LoginRsp;
import com.ig.spring_boot_learning.dto.UserDto;
import com.ig.spring_boot_learning.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ObjectResponse<LoginRsp> login(LoginReq loginDto);
    MessageResponse registerUser(Users req);
    MessageResponse updateUser(Long id, UserDto req);
}
