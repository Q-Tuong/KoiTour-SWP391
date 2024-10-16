package com.koitourdemo.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "koi_id")
    Koi koi;

    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal (quantity));
    }
}
