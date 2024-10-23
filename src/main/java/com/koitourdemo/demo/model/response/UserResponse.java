package com.koitourdemo.demo.model.response;

import com.koitourdemo.demo.enums.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    Role role;
    String token;
}
