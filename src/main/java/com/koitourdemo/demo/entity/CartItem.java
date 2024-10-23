package com.koitourdemo.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem implements Serializable {

    @Id
    UUID koiId;

    String productName;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

    public void updateTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }

}
