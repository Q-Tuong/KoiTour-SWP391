package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Tour findTourById(long id);

//    List<Tour> findToursByIsDeletedFalse();

    Page<Tour> findAll(Pageable pageable);

    @Query("SELECT t.name, SUM(tod.quantity) AS totalSold " +
            "FROM TourOrderDetail tod " +
            "JOIN tod.tour t " +
            "GROUP BY t.id, t.name " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> findTop5BestSellingTour();
}
