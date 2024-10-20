package com.koitourdemo.demo.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourRequest {

    @NotBlank(message = "Code cannot be blank!")
    @Pattern(regexp = "DF\\d{6}", message = "Code invalid!")
    @Column(unique = true)
    String code;

    String name;
    BigDecimal price;
    String description;
    String image;
    Date createAt;
}
