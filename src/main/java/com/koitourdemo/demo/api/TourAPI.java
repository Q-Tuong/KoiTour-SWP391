package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Tour;
import com.koitourdemo.demo.model.request.TourRequest;
import com.koitourdemo.demo.model.response.TourPageResponse;
import com.koitourdemo.demo.model.response.TourResponse;
import com.koitourdemo.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TourAPI {

    @Autowired
    TourService tourService;

    @PostMapping("/create")
    public ResponseEntity createTour(@Valid @RequestBody TourRequest tour){
        Tour newTour = tourService.createNewTour(tour);
        return ResponseEntity.ok(newTour);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllTour(@RequestParam int page, @RequestParam(defaultValue = "5") int size){
        TourPageResponse tourResponse = tourService.getAllTour(page, size);
        return ResponseEntity.ok(tourResponse);
    }

    @PutMapping("/{tourId}/update")
    public ResponseEntity updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long tourId){
        TourResponse updated = tourService.updateTour(tourRequest, tourId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tourId}/delete")
    public ResponseEntity deleteTour(@PathVariable long tourId){
        Tour deleted = tourService.deleteTour(tourId);
        return ResponseEntity.ok(deleted);
    }
}
