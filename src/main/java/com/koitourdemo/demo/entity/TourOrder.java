package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "TourOders")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    Date dateStarted;
    Date dateEnded;
    BigDecimal totalPrice;
    String note;
    Boolean status;
}
