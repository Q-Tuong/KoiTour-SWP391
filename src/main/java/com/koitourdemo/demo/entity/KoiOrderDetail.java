package com.koitourdemo.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "KoiOrderDetails")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    float unitPrice;
    float totalPrice;
    int quantity;
    String productName;

    @ManyToOne
    @JoinColumn(name = "order_id")
    KoiOrder order;

    @ManyToOne
    @JoinColumn(name = "koi_id")
    Koi koi;
}
