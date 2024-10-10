package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Orders;
import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {

    List<Orders> findOrderssByCustomer(User customer);

}
