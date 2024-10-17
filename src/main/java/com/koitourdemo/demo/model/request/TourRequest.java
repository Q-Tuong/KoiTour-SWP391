package com.koitourdemo.demo.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourRequest {
    String code;
    String name;
    float price;
    String description;
    String image;
    Date createAt;
}
