package com.ig.spring_boot_learning.dto;

import com.ig.spring_boot_learning.model.Role;
import com.ig.spring_boot_learning.model.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    private String password;
    private List<Long> roles;

    public Users toUser() {
        var roleObjs = roles.stream().map(Role::new).collect(Collectors.toSet());
        return new Users(id, firstName, lastName, email, username, password, roleObjs);
    }

    public Users updateUser(Users u) {
        var roleObjs = roles.stream().map(Role::new).collect(Collectors.toSet());
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setUsername(username);
        u.setEmail(email);
        u.getRoles().clear();
        u.getRoles().addAll(roleObjs);
        return u;
    }
}
