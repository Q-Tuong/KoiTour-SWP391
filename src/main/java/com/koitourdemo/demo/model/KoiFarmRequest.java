package com.koitourdemo.demo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiFarmRequest {

    String name;
    String address;
    String phone;
    String email;
    String description;
    String image;

}
