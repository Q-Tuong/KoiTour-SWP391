package com.koitourdemo.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem implements Serializable {

    @Id
    UUID koiId;

    String productName;
    int quantity = 1;
    float unitPrice;

}
