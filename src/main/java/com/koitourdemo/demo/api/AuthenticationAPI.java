package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.model.request.*;
import com.koitourdemo.demo.model.response.UserResponse;
import com.koitourdemo.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/user")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest){
        UserResponse newUser = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest){
        UserResponse newUser = authenticationService.login(loginRequest);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllUser(){
        List<User> users = authenticationService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity updateUser(@PathVariable long userId, @RequestBody UserRequest userRequest) {
        UserResponse updated = authenticationService.updateUser(userRequest, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{userId}/me")
    public ResponseEntity getUserById(@PathVariable long userId) {
        UserResponse userResponse = authenticationService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest){
        authenticationService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Forgot password sucessfully!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Reset password sucessfully!");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authenticationService.verifyEmail(token);
        return ResponseEntity.ok("Email has been successfully verified!");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam("email") String email) {
        authenticationService.resendVerificationEmail(email);
        return ResponseEntity.ok("Verify email has been resent. Please check your mail box again!");
    }

}
