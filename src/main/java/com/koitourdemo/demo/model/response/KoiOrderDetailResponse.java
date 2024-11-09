package com.koitourdemo.demo.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiOrderDetailResponse {
    String productName;
    int quantity;
    float unitPrice;
    float totalPrice;
    String imgUrl;
}
