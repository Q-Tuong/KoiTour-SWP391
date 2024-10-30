package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    String code;
    String name;
    String duration;
    String startAt;
    String description;
    float price;
    String imgUrl;
    Date createAt;
}
