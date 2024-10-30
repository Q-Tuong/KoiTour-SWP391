package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiOrder;
import com.koitourdemo.demo.enums.OrderStatus;
import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface KoiOrderRepository extends JpaRepository<KoiOrder, UUID> {
    List<KoiOrder> findKoiOrderByCustomer(User customer);

    List<KoiOrder> findByCustomerEmail(String email);

    List<KoiOrder> findByStatus(OrderStatus status);

    @Query("SELECT o FROM KoiOrder o WHERE o.status = :status AND o.createAt <= :checkDate")
    List<KoiOrder> findTimeoutOrders(@Param("status") OrderStatus status, @Param("checkDate") Date checkDate);

}
