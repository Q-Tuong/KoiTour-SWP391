package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.TourOrder;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TourOrderRepository extends JpaRepository<TourOrder, UUID> {
    List<TourOrder> findTourOderByCustomer(User customer);

    List<TourOrder> findByCustomerEmail(String email);

    List<TourOrder> findByStatus(OrderStatus status);

    @Query("SELECT o FROM TourOrder o WHERE o.status = :status AND o.createAt <= :checkDate")
    List<TourOrder> findTimeoutOrders(@Param("status") OrderStatus status, @Param("checkDate") Date checkDate);

}
