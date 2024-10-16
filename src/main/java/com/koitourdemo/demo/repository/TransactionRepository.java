package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transactions, UUID> {
}
