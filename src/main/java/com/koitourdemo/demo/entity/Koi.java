package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "Kois")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Koi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    @JsonIgnore
    boolean isDeleted = false;

    @NotBlank(message = "Koi name cannot be blank!")
    String name;

    @NotBlank(message = "Koi farm cannot be blank!")
    String farmName;

    @NotBlank(message = "Koi type cannot blank!")
    String type;

    @NotBlank(message = "Koi size cannot be blank!")
    String size;

    @NotBlank(message = "Koi origin cannot be blank!")
    String origin;

    String image;
    BigDecimal price;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "koiFarm_id")
    KoiFarm koiFarm;

    @OneToMany(mappedBy = "koi")
    @JsonIgnore
    List<OrderDetail> orderDetails;
}
