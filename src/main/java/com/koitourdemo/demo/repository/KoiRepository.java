package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Koi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface KoiRepository extends JpaRepository<Koi, UUID> {

    Koi findKoiById(UUID id);

//    List<Koi> findKoisByIsDeletedFalse();

    Page<Koi> findAll(Pageable pageable);

    @Query("SELECT p.name, SUM(kod.quantity) AS totalSold FROM KoiOrderDetail kod " +
            "JOIN kod.koi p GROUP BY p.id " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> findTop5BestSellingKoi();
}
