package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiOrder;
import com.koitourdemo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KoiOrderRepository extends JpaRepository<KoiOrder, UUID> {

    List<KoiOrder> findKoiOrderByCustomer(User customer);

}
