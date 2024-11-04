package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Koi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface KoiRepository extends JpaRepository<Koi, UUID> {

    Koi findKoiById(UUID id);

//    List<Koi> findKoisByIsDeletedFalse();

    @Query("SELECT k FROM Koi k WHERE k.isDeleted = false")
    Page<Koi> findAll(Pageable pageable);

    @Query("SELECT k FROM Koi k WHERE k.isDeleted = false AND " +
            "(LOWER(k.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.origin) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(k.size AS string) LIKE CONCAT('%', :keyword, '%') OR " +
            "CAST(k.price AS string) LIKE CONCAT('%', :keyword, '%'))")
    Page<Koi> searchKoi(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT k.name, SUM(kod.quantity) AS totalSold " +
            "FROM KoiOrderDetail kod " +
            "JOIN kod.koi k " +
            "GROUP BY k.id, k.name " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> findTop5BestSellingKoi();
}
