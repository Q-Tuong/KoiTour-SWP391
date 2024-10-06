package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table(name = "Tours")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long tourId;

    String tourName;
    BigDecimal tourPrice;
    LocalDate tourDate;
    String tourDescription;
}
