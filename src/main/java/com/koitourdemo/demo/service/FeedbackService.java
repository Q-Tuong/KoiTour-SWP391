package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.entity.Feedback;
import com.koitourdemo.demo.model.request.FeedbackRequest;
import com.koitourdemo.demo.model.response.FeedbackResponse;
import com.koitourdemo.demo.repository.UserRepository;
import com.koitourdemo.demo.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    public Feedback createNewFeedback(FeedbackRequest feedbackRequest){
        User consulting = userRepository.findById(feedbackRequest.getShopId())
                .orElseThrow(() -> new EntityNotFoundException("shop not found!"));
        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequest.getContent());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCustomer(authenticationService.getCurrentUser());
        feedback.setConsulting(consulting);
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackResponse> getFeedback(){
        return feedbackRepository.findFeedbackByFarmId(authenticationService.getCurrentUser().getId());
    }

}
