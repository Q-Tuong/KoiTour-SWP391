package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiOrder;
import com.koitourdemo.demo.entity.Payment;
import com.koitourdemo.demo.entity.TourOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByKoiOrder(KoiOrder koiOrder);

    List<Payment> findByTourOrder(TourOrder tourOrder);
}
