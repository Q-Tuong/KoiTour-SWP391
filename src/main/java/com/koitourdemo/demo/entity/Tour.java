package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "Tours")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    String name;
    float price;
    Date createAt;
    String description;
}
