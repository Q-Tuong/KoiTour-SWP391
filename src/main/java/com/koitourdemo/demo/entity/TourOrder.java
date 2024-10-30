package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koitourdemo.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    float total;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    User customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    List<TourOrderDetail> TourOrderDetails;

    @OneToOne(mappedBy = "tourOrder")
    @JsonIgnore
    Payment tourPayment;
}
