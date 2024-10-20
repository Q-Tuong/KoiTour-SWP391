package com.koitourdemo.demo.service;

import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    public User updateUserRole(Role newRole, long id) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getId() == id) {
            throw new NotFoundException("Không thể sửa role của chính mình!");
        }

        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User deleteUser(long id){
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getId() == id) {
            throw new NotFoundException("Không thể xóa tài khoản của chính mình!");
        }

        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if (oldUser.isDeleted()) {
            throw new NotFoundException("Người dùng này đã bị xóa trước đó!");
        }
        oldUser.setDeleted(true);
        return userRepository.save(oldUser);
    }

}
