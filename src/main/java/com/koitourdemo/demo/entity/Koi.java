package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "KoiList")
@Entity
public class Koi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long koiId;

    @JsonIgnore
    boolean isDeleted = false;

    String koiName;
    String koiColor;
    String koiWeight;
    String koiSize;
    String koiOrigin;
    String koiDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
