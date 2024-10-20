package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiFarmResponse {
    String name;
    String address;
    String phone;
    String description;
    String image;
    Date createAt;
}
