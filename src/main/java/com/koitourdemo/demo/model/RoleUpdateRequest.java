package com.koitourdemo.demo.model;

import com.koitourdemo.demo.entity.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {
    Role role;
}
