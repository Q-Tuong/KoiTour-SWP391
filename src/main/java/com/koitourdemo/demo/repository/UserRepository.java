package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByPhone(String userPhone);

    User findUserById(long id);

    User findUserByEmail(String email);
}
