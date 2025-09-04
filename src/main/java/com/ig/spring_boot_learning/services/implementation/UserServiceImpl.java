package com.ig.spring_boot_learning.services.implementation;

import com.ig.spring_boot_learning.base.response.MessageResponse;
import com.ig.spring_boot_learning.base.response.ObjectResponse;
import com.ig.spring_boot_learning.dto.LoginReq;
import com.ig.spring_boot_learning.dto.LoginRsp;
import com.ig.spring_boot_learning.dto.UserDto;
import com.ig.spring_boot_learning.exceptions.ApiErrorException;
import com.ig.spring_boot_learning.model.Users;
import com.ig.spring_boot_learning.model.auth.UserPrinciple;
import com.ig.spring_boot_learning.repositories.UserRepository;
import com.ig.spring_boot_learning.security.JwtUtilProvider;
import com.ig.spring_boot_learning.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrinciple();
    }

    @Override
    public ObjectResponse<LoginRsp> login(LoginReq loginReq) {
        final var user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCaseAndStatusTrue(loginReq.username(), loginReq.username()).orElseThrow(() ->
                new ApiErrorException(401, "username or password incorrect"));
        if (!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
            throw new ApiErrorException(401, "username or password incorrect");
        }
        final var userAuthorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList();
        final var userDetail = new UserPrinciple(
                user.getId(), user.getUsername(), user.getPassword(), userAuthorities
        );
        final var token = jwtProvider.generateToken(userDetail);
        return new ObjectResponse<>(new LoginRsp(token));
    }

    @Override
    @Transactional
    public MessageResponse registerUser(Users req) {
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(req);
        return new MessageResponse();
    }

    @Override
    @Transactional
    public MessageResponse updateUser(Long id, UserDto req) {
        final var user = userRepository.findByIdAndStatusTrue(id).orElseThrow(() -> new ApiErrorException(404, "User not found"));
        var userUpdated = req.updateUser(user);
        userRepository.save(userUpdated);
        return new MessageResponse();
    }
}
