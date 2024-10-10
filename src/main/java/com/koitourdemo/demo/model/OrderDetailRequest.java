package com.koitourdemo.demo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    UUID koiID;
    int quantity;
}
