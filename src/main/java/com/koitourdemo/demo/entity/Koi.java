package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Table(name = "KoiList")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Koi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long koiId;

    @JsonIgnore
    boolean isDeleted = false;

    @NotBlank(message = "Koi name cannot be blank!")
    String koiName;

    @NotBlank(message = "Koi color cannot be blank!")
    String koiColor;

    @NotBlank(message = "Koi weight cannot be blank!")
    String koiWeight;

    @NotBlank(message = "Koi size cannot be blank!")
    String koiSize;

    @NotBlank(message = "Koi origin cannot be blank!")
    String koiOrigin;

    String koiDescription;
    String koiImageURL;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
