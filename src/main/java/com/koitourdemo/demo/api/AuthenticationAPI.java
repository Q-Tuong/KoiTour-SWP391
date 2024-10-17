package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.ForgotPasswordRequest;
import com.koitourdemo.demo.model.request.LoginRequest;
import com.koitourdemo.demo.model.request.RegisterRequest;
import com.koitourdemo.demo.model.request.ResetPasswordRequest;
import com.koitourdemo.demo.model.response.ApiResponse;
import com.koitourdemo.demo.model.response.UserResponse;
import com.koitourdemo.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("api/user")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            UserResponse newUser = authenticationService.register(registerRequest);
            return ResponseEntity.ok(new ApiResponse("Register successfully! Please check your mailbox for verify!", newUser));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to register!", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            UserResponse newUser = authenticationService.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse("Login successfully!", newUser));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to login!", null));
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllUser(){
        try {
            List<User> users = authenticationService.getAllUser();
            return ResponseEntity.ok(new ApiResponse("Get all users successfully!", users));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to get all users!", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authenticationService.logout(token);
        return ResponseEntity.ok("Logout successfully!");
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
