package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Role;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.DuplicateEntity;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.*;
import com.koitourdemo.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    public UserResponse register(RegisterRequest registerRequest){
        User user = modelMapper.map(registerRequest, User.class);
        try{
            String originPassword = user.getUserPassword();
            user.setUserPassword(passwordEncoder.encode(originPassword));
            user.setRole(Role.CUSTOMER);
            User newUser = userRepository.save(user);

            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(newUser);
            emailDetail.setSubject("Welcome to rạp xiếc trung ương! we are excited to have you");
            emailDetail.setLink("https://www.google.com/");
            emailService.sendEmail(emailDetail);

            return modelMapper.map(newUser, UserResponse.class);
        }catch (Exception e){
            if(e.getMessage().contains(user.getUserCode())) {
                throw new DuplicateEntity("Duplicate code!");
            }else if (e.getMessage().contains(user.getUserEmail())) {
                throw new DuplicateEntity("Duplicate email!");
            }else{
                throw new DuplicateEntity("Duplicate phone!");
            }
        }
    }

    public UserResponse login(LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));

            User user = (User) authentication.getPrincipal();
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponse.setToken(tokenService.generateToken(user));
            return userResponse;
        }catch (Exception e){
            throw new EntityNotFoundException("Username or password invalid!");
        }
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User login() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String userPhone) throws UsernameNotFoundException {
        return userRepository.findUserByUserPhone(userPhone);
    }

    public User getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUserId(user.getUserId());
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest){

        User user = userRepository.findUserByUserEmail(forgotPasswordRequest.getUserEmail());

        if(user == null){
            throw new NotFoundException("email not found!");
        }else{
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(user);
            emailDetail.setSubject("Reset password");
            emailDetail.setLink("https://www.facebook.com/" + tokenService.generateToken(user));
            emailService.sendEmail(emailDetail);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        User user = getCurrentUser();
        user.setUserPassword(passwordEncoder.encode(resetPasswordRequest.getUserPassword()));
        userRepository.save(user);
    }

}
