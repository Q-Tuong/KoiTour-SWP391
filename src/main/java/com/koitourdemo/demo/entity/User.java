package com.koitourdemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.koitourdemo.demo.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Table(name = "Users")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    @Enumerated(EnumType.STRING)
    Role role;

    @JsonIgnore
    boolean isDeleted = false;

//    @NotBlank(message = "FirstName cannot be blank!")
//    String firstName;
//
//    @NotBlank(message = "LastName cannot be blank!")
//    String lastName;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 6, message = "Password must be at least 6 digits!")
    String password;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone number!")
    @Column(unique = true)
    String phone;

    @NotBlank(message = "Address cannot be blank!")
    String address;

    @JsonIgnore
    boolean emailVerified;
    float balance = 0;
    Date createAt;
    String verificationToken;
    Date verificationTokenExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.role != null) authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Koi> kois;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<KoiFarm> koiFarms;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Tour> tours;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    List<Orders> orders;

    @OneToMany(mappedBy = "from")
    @JsonIgnore
    Set<Transactions> transactionsFrom;

    @OneToMany(mappedBy = "to")
    @JsonIgnore
    Set<Transactions> transactionsTo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    Cart cart;
}
