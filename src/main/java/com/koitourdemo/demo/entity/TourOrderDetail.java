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
@Table(name = "TourOrderDetails")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    float unitPrice;
    float totalPrice;
    int quantity;
    String tourName;
    String duration;
    String startAt;
    String description;

    @ManyToOne
    @JoinColumn(name = "order_id")
    TourOrder order;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    Tour tour;
}
