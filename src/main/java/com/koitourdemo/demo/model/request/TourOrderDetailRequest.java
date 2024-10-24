package com.koitourdemo.demo.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourOrderDetailRequest {
    long tourId;
    int quantity;
    float unitPrice;
    float totalPrice;
}
