package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiFarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiFarmRepository extends JpaRepository<KoiFarm, Long> {

    KoiFarm findKoiFarmByKoiFarmId(long koiFarmId);

    List<KoiFarm> findKoiFarmsByIsDeletedFalse();
}
