package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiFarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiFarmRepository extends JpaRepository<KoiFarm, Long> {

    KoiFarm findKoiFarmById(long id);

    List<KoiFarm> findKoiFarmsByIsDeletedFalse();
}