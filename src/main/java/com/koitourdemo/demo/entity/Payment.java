package com.koitourdemo.demo.entity;

import com.koitourdemo.demo.enums.PaymentEnums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date createAt;

    @Enumerated(EnumType.STRING)
    PaymentEnums paymentMethod;

    @OneToOne
    @JoinColumn(name = "koi_order_id")
    KoiOrder koiOrder;

    @OneToOne
    @JoinColumn(name = "tour_order_id")
    TourOrder tourOrder;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Transactions> transactions;
}
