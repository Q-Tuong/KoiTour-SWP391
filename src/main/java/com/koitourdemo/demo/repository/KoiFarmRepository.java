package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiFarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KoiFarmRepository extends JpaRepository<KoiFarm, Long> {

    KoiFarm findKoiFarmById(long id);

//    List<KoiFarm> findKoiFarmsByIsDeletedFalse();

    @Query("SELECT f FROM KoiFarm f WHERE f.isDeleted = false")
    Page<KoiFarm> findAll(Pageable pageable);

    @Query("SELECT f FROM KoiFarm f WHERE f.isDeleted = false AND " +
            "LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<KoiFarm> searchKoiFarm(@Param("keyword") String keyword, Pageable pageable);

    Optional<KoiFarm> findByNameContainingIgnoreCase(String name);
}
