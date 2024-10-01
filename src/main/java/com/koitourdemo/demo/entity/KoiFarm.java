package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "KoiFarms")
@Entity
public class KoiFarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long koiFarmId;

    private String koiFarmName;
    private String koiFarmAddress;
    private String koiFarmPhone;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    private String koiFarmEmail;

    private String koiFarmDescription;
    private String koiFarmImageURL;
}
