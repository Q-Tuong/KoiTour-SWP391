package com.koitourdemo.demo.entity;

import com.koitourdemo.demo.enums.TransactionsEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    TransactionsEnum status;

    float amount;
    String description;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "from_id")
    User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    User to;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment tourPayment;
}
