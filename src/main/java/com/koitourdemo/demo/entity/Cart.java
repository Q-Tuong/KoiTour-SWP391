package com.koitourdemo.demo.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart implements Serializable {

    public final long serialVersionUID = 1L;
    List<CartItem> items = new ArrayList<>();

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        Optional<CartItem> existingItem = items.stream()
                .filter(i -> i.getKoiId().equals(item.getKoiId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem existing = existingItem.get();
            existing.setQuantity(existing.getQuantity() + 1);
        } else {
            items.add(item);
        }
    }

    public void removeItem(UUID koiId) {
        items.removeIf(item -> item.getKoiId().equals(koiId));
    }
}
