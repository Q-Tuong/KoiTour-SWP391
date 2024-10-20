package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiResponse {
    String name;
    String farmName;
    String type;
    String size;
    String origin;
    BigDecimal price;
    String image;
    Date createAt;
}
