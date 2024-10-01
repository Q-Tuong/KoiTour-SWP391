package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tours")
@Entity
public class Tour {

    //    @ManyToOne
    //    @JoinColumn(name = "type_id", nullable = false)
    //    private  tourType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long tourId;

    private String tourName;
    private BigDecimal tourPrice;
    private LocalDate tourDate;
    private String tourDescription;
}
