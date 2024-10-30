package com.koitourdemo.demo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiOrderDetailRequest {
    UUID koiId;
    int quantity;

    @JsonIgnore
    float unitPrice;

    @JsonIgnore
    float totalPrice;
}
