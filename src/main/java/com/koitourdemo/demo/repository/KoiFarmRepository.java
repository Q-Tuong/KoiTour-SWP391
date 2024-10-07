package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiFarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KoiFarmRepository extends JpaRepository<KoiFarm, Long> {
}
