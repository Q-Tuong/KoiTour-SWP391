package com.koitourdemo.demo.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiRequest {
    String name;
    String farmName;
    String type;
    String color;
    String size;
    String origin;
    BigDecimal price;
    String image;
    Date createAt;

}
