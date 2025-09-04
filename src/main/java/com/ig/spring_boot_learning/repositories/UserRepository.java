package com.ig.spring_boot_learning.repositories;

import com.ig.spring_boot_learning.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByIdAndStatusTrue(Long id);
    public Optional<Users> findByUsernameIgnoreCaseOrEmailIgnoreCaseAndStatusTrue(String username, String email);
}
