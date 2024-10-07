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
    long koiFarmId;

    @JsonIgnore
    boolean isDeleted = false;

    @NotBlank(message = "Koi farm name cannot be blank!")
    String koiFarmName;

    @NotBlank(message = "Koi farm address cannot be blank!")
    String koiFarmAddress;

    @NotBlank(message = "Invalid phone number!")
    String koiFarmPhone;

    @Email(message = "Invalid email!")
    @Column(unique = true)
    String koiFarmEmail;

    String koiFarmDescription;
//  String koiFarmImageURL;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
