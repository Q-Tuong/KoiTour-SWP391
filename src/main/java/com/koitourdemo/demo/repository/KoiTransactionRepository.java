package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface KoiTransactionRepository extends JpaRepository<KoiTransaction, UUID> {

    @Query("SELECT YEAR(t.createAt) AS year, MONTH(t.createAt) AS month, sum(t.amount)" +
            "FROM TourTransaction t " +
            "WHERE t.status = 'SUCCESS' AND t.to.id =: userId " +
            "GROUP BY YEAR(t.createAt), MONTH(t.createAt) " +
            "ORDER BY YEAR(t.createAt), MONTH(t.createAt)")
    List<Object[]> calculateMonthlyRevenue(@Param("userId") Long userId);
}
