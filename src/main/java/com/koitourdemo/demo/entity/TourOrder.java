package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "TourOders")
@Entity
public class TourOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long tourOrderId;

    private Date dateStarted;
    private Date dateEnded;
    private BigDecimal totalPrice;
    private String note;
    private Boolean status;
}
