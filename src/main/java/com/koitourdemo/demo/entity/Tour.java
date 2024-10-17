package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "Tours")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    @JsonIgnore
    boolean isDeleted = false;

    @NotBlank(message = "Code cannot be blank!")
    @Pattern(regexp = "DF\\d{6}", message = "Code invalid!")
    @Column(unique = true)
    String code;

    @NotBlank(message = "Tour name cannot be blank!")
    String name;

    @NotBlank(message = "Tour price cannot be blank!")
    float price;

    Date createAt;
    String image;
    String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    List<OrderDetail> orderDetails;
}
