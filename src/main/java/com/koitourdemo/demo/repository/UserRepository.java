package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserPhone(String userPhone);

    User findUserByUserId(long userId);

    User findUserByUserEmail(String userEmail);
}
