package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiResponse {
    String name;
    String type;
    String size;
    String origin;
    float price;
    String imgUrl;
    Date createAt;
}
