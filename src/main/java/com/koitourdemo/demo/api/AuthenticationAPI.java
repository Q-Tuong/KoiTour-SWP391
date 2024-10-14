package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.model.*;
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

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        UserResponse newUser = authenticationService.register(registerRequest);
        return ResponseEntity.ok(new ApiResponse("Register successfully!", newUser));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UserResponse newUser = authenticationService.login(loginRequest);
        return ResponseEntity.ok(new ApiResponse("Login successfully!", newUser));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllUser(){
        List<User> users = authenticationService.getAllUser();
        return ResponseEntity.ok(new ApiResponse("Successfully!", users));
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
}
