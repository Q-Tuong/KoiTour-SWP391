package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.KoiTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<KoiTransaction, UUID> {
}
