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

    @NotBlank(message = "Koi color cannot be blank!")
    String color;

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

    @ManyToMany
    @JoinTable(name = "Kois_Farms",
        joinColumns = @JoinColumn(name = "koi_id"),
        inverseJoinColumns = @JoinColumn(name = "koiFarm_id")
    )
    List<KoiFarm> koiFarms;

    @OneToMany(mappedBy = "koi")
    @JsonIgnore
    List<OrderDetail> orderDetails;
}
