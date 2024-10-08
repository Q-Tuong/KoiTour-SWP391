package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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

    @Email(message = "Invalid email!")
    @Column(unique = true)
    String email;

    String description;
    String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
