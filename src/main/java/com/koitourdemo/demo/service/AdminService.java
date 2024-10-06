package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Role;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    public User updateUserRole(Role newRole, long userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User deleteUser(long userId){
        User oldUser = getUserByUserId(userId);
        oldUser.setDeleted(true);
        return userRepository.save(oldUser);
    }

    public User getUserByUserId(long userId){
        User oldUser = userRepository.findUserByUserId(userId);
        if(oldUser == null)
            throw new NotFoundException("User not found!");
        return oldUser;
    }

}
