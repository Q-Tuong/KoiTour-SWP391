package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KoiRepository extends JpaRepository<Koi, UUID> {

    Koi findKoiById(UUID id);

    List<Koi> findKoisByIsDeletedFalse();
}
