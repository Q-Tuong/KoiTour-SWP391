package com.koitourdemo.demo.model.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {

    @Size(min = 6, message = "Password must be at least 6 digits!")
    String password;
}
