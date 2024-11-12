package com.koitourdemo.demo.service;

import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.AuthException;
import com.koitourdemo.demo.exception.DuplicateEntity;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.*;
import com.koitourdemo.demo.model.request.*;
import com.koitourdemo.demo.model.response.UserResponse;
import com.koitourdemo.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

import java.util.Date;
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

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    public UserResponse register(RegisterRequest registerRequest){
            // Kiểm tra xem email đã tồn tại chưa
            User existingUser = userRepository.findUserByEmail(registerRequest.getEmail());
            if (existingUser != null) {
                if (existingUser.isDeleted()) {
                    throw new AuthException("This account with current email has been disabled!");
                }
                if (existingUser.isEmailVerified()) {
                    throw new DuplicateEntity("This email has been used!");
                } else {
                    // Nếu tài khoản tồn tại nhưng chưa xác thực, gửi lại email xác thực
                    resendVerificationEmail(existingUser.getEmail());
                    throw new AuthException("Account already exists but has not been verified. The verification email has been resent!");
                }
            }
            User user = modelMapper.map(registerRequest, User.class);
            try {
                String originPassword = user.getPassword();
                user.setPassword(passwordEncoder.encode(originPassword));
                user.setRole(Role.ADMIN);
                user.setEmailVerified(false);
                user.setVerificationToken(tokenService.generateToken(user));
                user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + 1000 * 60 * 5)); // 5 phút
                User newUser = userRepository.save(user);

                EmailDetail emailDetail = new EmailDetail();
                emailDetail.setReceiver(newUser);
                emailDetail.setSubject("Please verify your account!");
                emailDetail.setLink("facebook.com?token=" + newUser.getVerificationToken());
                emailService.sendEmail(emailDetail);

                return modelMapper.map(newUser, UserResponse.class);
            } catch (Exception e) {
            if (e.getMessage().contains(user.getEmail())) {
                throw new DuplicateEntity("Duplicate email!");
            }else{
                throw new DuplicateEntity("Duplicate phone!");
            }
        }
    }

    public UserResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
            User user = (User) authentication.getPrincipal();
            if (user.isDeleted()) {
                throw new NotFoundException("!");
            }
            if (!user.isEmailVerified()) {
                throw new AuthException("!");
            }
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponse.setToken(tokenService.generateToken(user));
            return userResponse;
        } catch (NotFoundException e) {
            throw new NotFoundException("This account has been disabled!");
        } catch (AuthException e) {
            throw new AuthException("Account is not verified!");
        } catch (Exception e) {
            throw new EntityNotFoundException("Invalid username or password!");
        }
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not existed!");
        }
        return user;
    }

    public UserResponse getCurrentUserInfo() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("Current user not found!");
        }
        return modelMapper.map(currentUser, UserResponse.class);
    }

    public User getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserById(user.getId());
    }

    public UserResponse updateUser(UserRequest userRequest, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy người dùng này!"));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponse.class);
    }

    public UserResponse getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot found this user!"));
        return modelMapper.map(user, UserResponse.class);
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest){
        User user = userRepository.findUserByEmail(forgotPasswordRequest.getEmail());
        if(user == null){
            throw new NotFoundException("Email not found!");
        }else{
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(user);
            emailDetail.setSubject("Reset password");
//            emailDetail.setLink("https://www.facebook.com?token=" + tokenService.generateToken(user));
            emailService.sendEmail(emailDetail);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
    }

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            throw new AuthException("Invalid verify code!");
        }
        if (user.isEmailVerified()) {
            throw new AuthException("Account has been verified");
        }
        if (user.getVerificationTokenExpiry().before(new Date())) {
            throw new AuthException("Verify code has been expired. Please resend code");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
    }

    public void resendVerificationEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("Email does not exist!");
        }
        if (user.isEmailVerified()) {
            throw new AuthException("Email has been verified!");
        }

        // Tạo token mới và cập nhật thời gian hết hạn
        user.setVerificationToken(tokenService.generateToken(user));
        user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + 1000 * 60 * 5)); // 5 phút
        userRepository.save(user);

        // Gửi lại email xác thực
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setReceiver(user);
        emailDetail.setSubject("Please verify your account!");
        emailDetail.setLink("https://www.facebook.com?token=" + user.getVerificationToken());
        emailService.sendEmail(emailDetail);
    }

}
