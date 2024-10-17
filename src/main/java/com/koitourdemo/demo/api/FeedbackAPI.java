package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Feedback;
import com.koitourdemo.demo.model.request.FeedbackRequest;
import com.koitourdemo.demo.model.response.FeedbackResponse;
import com.koitourdemo.demo.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedbackAPI {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody FeedbackRequest feedbackRequest){
        Feedback feedback = feedbackService.createNewFeedback(feedbackRequest);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/get")
    public ResponseEntity getFeedback(){
        List<FeedbackResponse> feedback = feedbackService.getFeedback();
        return ResponseEntity.ok(feedback);
    }
}
