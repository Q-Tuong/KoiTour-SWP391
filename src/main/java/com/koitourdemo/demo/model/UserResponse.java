package com.koitourdemo.demo.model;

import lombok.Data;

@Data
public class UserResponse {
    long userId;
    String userCode;
    String userPhone;
    String userEmail;
    String token;
}
