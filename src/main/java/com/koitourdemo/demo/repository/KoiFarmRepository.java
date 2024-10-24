package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiFarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KoiFarmRepository extends JpaRepository<KoiFarm, Long> {

    KoiFarm findKoiFarmById(long id);

//    List<KoiFarm> findKoiFarmsByIsDeletedFalse();

    Page<KoiFarm> findAll(Pageable pageable);

    Optional<KoiFarm> findByNameContainingIgnoreCase(String name);
}
