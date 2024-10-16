package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(long id);
}
