package com.koitourdemo.demo.entity;

import com.koitourdemo.demo.enums.TransactionsEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    TransactionsEnum status;
    String description;

    @ManyToOne
    @JoinColumn(name = "from_id")
    User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    User to;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

}
