package com.koitourdemo.demo.entity;

import com.koitourdemo.demo.enums.PaymentEnums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date createAt;

    PaymentEnums paymentMethod;

    @OneToOne
    @JoinColumn(name = "orders_id")
    Orders orders;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Transactions> transactions;

}
