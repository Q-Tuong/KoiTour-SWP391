package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Tour findTourById(long id);

    List<Tour> findToursByIsDeletedFalse();
}
