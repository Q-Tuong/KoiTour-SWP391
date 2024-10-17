package com.koitourdemo.demo.repository;

import com.koitourdemo.demo.entity.Feedback;
import com.koitourdemo.demo.model.response.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT new com.koitourdemo.demo.model.response.FeedbackResponse(f.id, f.content, f.rating, u.email) " +
           "FROM Feedback f join User u on f.consulting.id = u.id WHERE f.consulting.id =: consultingId ")
    List<FeedbackResponse> findFeedbackByFarmId(@Param("consultingId") Long consultingId);
}
