package com.koitourdemo.demo.api;

import com.koitourdemo.demo.model.request.*;
import com.koitourdemo.demo.model.response.UserResponse;
import com.koitourdemo.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update/{userId}")
    public ResponseEntity updateUser(@PathVariable long userId, @RequestBody UserRequest userRequest) {
        UserResponse updated = authenticationService.updateUser(userRequest, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/get-current-user")
    public ResponseEntity getCurrentUser() {
        UserResponse userResponse = authenticationService.getCurrentUserInfo();
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
