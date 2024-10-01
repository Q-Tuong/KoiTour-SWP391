package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiRepository extends JpaRepository<Koi, Long> {

    Koi findKoiByKoiId(long koiId);

    List<Koi> findKoisByIsDeletedFalse();
}
