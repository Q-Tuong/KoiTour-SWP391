package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.TourOrder;
import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TourOrderRepository extends JpaRepository<TourOrder, UUID> {
    List<TourOrder> findTourOderByCustomer(User customer);
}
