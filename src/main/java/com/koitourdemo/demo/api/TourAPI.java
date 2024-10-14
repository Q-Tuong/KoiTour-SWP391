package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Tour;
import com.koitourdemo.demo.model.ApiResponse;
import com.koitourdemo.demo.model.TourRequest;
import com.koitourdemo.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/tour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TourAPI {

    @Autowired
    TourService tourService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTour(@Valid @RequestBody TourRequest tour){
        Tour newTour = tourService.createNewTour(tour);
        return ResponseEntity.ok(new ApiResponse("Create new tour successfully!", newTour));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllTour(){
        List<Tour> tours = tourService.getAllTour();
        return ResponseEntity.ok(new ApiResponse("Successfully!", tours));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> updateTour(@Valid @RequestBody Tour tour, @PathVariable long id){
        Tour updated = tourService.updateTour(tour, id);
        return ResponseEntity.ok(new ApiResponse("Update tour successfully!", updated));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteTour(@PathVariable long id){
        Tour deleted = tourService.deleteTour(id);
        return ResponseEntity.ok(new ApiResponse("Delete tour successfully!", deleted));
    }
}
