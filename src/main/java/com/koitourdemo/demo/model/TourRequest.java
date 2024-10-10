package com.koitourdemo.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourRequest {
    String name;
    float price;
    Date createAt;
    String description;
    String image;
}
