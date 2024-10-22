package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    String code;
    String name;
    String duration;
    String description;
    BigDecimal price;
    String image;
    Date createAt;
}
