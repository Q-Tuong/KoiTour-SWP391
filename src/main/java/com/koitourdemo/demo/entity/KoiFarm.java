package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Table(name = "KoiFarms")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiFarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    @JsonIgnore
    boolean isDeleted = false;

    @NotBlank(message = "Koi farm name cannot be blank!")
    String name;

    @NotBlank(message = "Koi farm address cannot be blank!")
    String address;

    @NotBlank(message = "Invalid phone number!")
    String phone;
    String description;
    String image;
    float balance = 0;
    Date createAt;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    User manager;

    @OneToMany(mappedBy = "koiFarm")
    @JsonIgnore
    List<Koi> kois;
}
