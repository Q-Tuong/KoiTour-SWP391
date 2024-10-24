package com.koitourdemo.demo.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiRequest {
    String name;
    String farmName;
    String type;
    String size;
    String origin;
    float price;
    String image;
    Date createAt;
}
