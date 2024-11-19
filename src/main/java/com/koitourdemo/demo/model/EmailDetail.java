package com.koitourdemo.demo.model;

import com.koitourdemo.demo.entity.KoiOrderDetail;
import com.koitourdemo.demo.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetail {
    User receiver;
    String subject;
    String link;

    String orderId;
    String createAt;
    String totalPrice;
    List<Map<String, Object>> orderDetails;
}
