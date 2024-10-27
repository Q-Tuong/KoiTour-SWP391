package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface KoiTransactionRepository extends JpaRepository<KoiTransaction, UUID> {

    @Query("SELECT YEAR(k.createAt) AS year, MONTH(k.createAt) AS month, sum(k.amount)" +
            "FROM KoiTransaction k " +
            "WHERE k.status = 'SUCCESS' AND k.to.id = :userId " +
            "GROUP BY YEAR(k.createAt), MONTH(k.createAt) " +
            "ORDER BY YEAR(k.createAt), MONTH(k.createAt)")
    List<Object[]> calculateMonthlyRevenue(@Param("userId") Long userId);
}
