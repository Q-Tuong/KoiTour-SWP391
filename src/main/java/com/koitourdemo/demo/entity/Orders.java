package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    float total;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    User customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "orders")
    @JsonIgnore
    Payment payment;
}
