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
    BigDecimal totalAmount = BigDecimal.ZERO;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addItem(CartItem item) {
        Optional<CartItem> existingItem = items.stream()
                .filter(i -> i.getKoiId().equals(item.getKoiId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem existing = existingItem.get();
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            existing.updateTotalPrice();
        } else {
            items.add(item);
        }
        updateTotalAmount();
    }

    public void removeItem(UUID koiId) {
        items.removeIf(item -> item.getKoiId().equals(koiId));
        updateTotalAmount();
    }

    public void updateItemQuantity(UUID koiId, int quantity) {
        items.stream()
                .filter(item -> item.getKoiId().equals(koiId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.updateTotalPrice();
                });
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
