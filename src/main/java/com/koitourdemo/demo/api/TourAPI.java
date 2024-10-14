package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Tour;
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
    public ResponseEntity createTour(@Valid @RequestBody TourRequest tour){
        Tour newTour = tourService.createNewTour(tour);
        return ResponseEntity.ok(newTour);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllTour(){
        List<Tour> tours = tourService.getAllTour();
        return ResponseEntity.ok(tours);
    }

    @PutMapping("{id}")
    public ResponseEntity updateTour(@Valid @RequestBody Tour tour, @PathVariable long id){
        Tour updated = tourService.updateTour(tour, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTour(@PathVariable long id){
        Tour deleted = tourService.deleteTour(id);
        return ResponseEntity.ok(deleted);
    }
}
