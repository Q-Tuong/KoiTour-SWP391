package com.koitourdemo.demo.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart implements Serializable {

    public final long serialVersionUID = 1L;
    List<CartItem> items = new ArrayList<>();
    float totalAmount = 0;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalAmount = 0;
    }

    public void removeItem(UUID koiId) {
        items.removeIf(item -> item.getKoiId().equals(koiId));
    }

}
