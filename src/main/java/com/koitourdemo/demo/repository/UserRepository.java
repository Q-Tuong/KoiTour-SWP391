package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByPhone(String userPhone);

    User findUserById(long id);

    User findUserByEmail(String email);

    User findByVerificationToken(String token);

    User findUserByRole(Role role);
}
