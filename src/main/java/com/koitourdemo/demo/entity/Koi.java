package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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

    @NotBlank(message = "Koi type cannot blank!")
    String type;

    @NotBlank(message = "Koi size cannot be blank!")
    String size;

    @NotBlank(message = "Koi origin cannot be blank!")
    String origin;

    String imgUrl;
    float price;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    User manager;

    @ManyToOne
    @JoinColumn(name = "koiFarm_id")
    KoiFarm koiFarm;

    @OneToMany(mappedBy = "koi")
    @JsonIgnore
    List<KoiOrderDetail> orderDetails;
}
