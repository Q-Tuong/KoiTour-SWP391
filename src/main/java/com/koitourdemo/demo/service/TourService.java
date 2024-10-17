package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Tour;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.TourRequest;
import com.koitourdemo.demo.model.response.TourPageResponse;
import com.koitourdemo.demo.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public Tour createNewTour(TourRequest tourRequest){
//        Tour tour = modelMapper.map(tourRequest, Tour.class);
        Tour tour = new Tour();
        tour.setCode(tour.getCode());
        tour.setName(tourRequest.getName());
        tour.setPrice(tour.getPrice());
        tour.setDescription(tour.getDescription());
        tour.setImage(tour.getImage());

        User userRequest = authenticationService.getCurrentUser();
        tour.setUser(userRequest);

        try {
            Tour newTour = tourRepository.save(tour);
            return newTour;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("error when saving tour to db");
        }
    }

    public TourPageResponse getAllTour(int page, int size){
        Page tourPage = tourRepository.findAllByIsDeletedFalse(PageRequest.of(page, size));
        TourPageResponse tourResponse = new TourPageResponse();
        tourResponse.setTotalPages(tourPage.getTotalPages());
        tourResponse.setContent(tourPage.getContent());
        tourResponse.setPageNumber(tourPage.getNumber());
        tourResponse.setTotalElements(tourPage.getTotalElements());
        return tourResponse;
    }

    public Tour updateTour(Tour tour, long id){
        Tour oldTour = getTourById(id);
        oldTour.setName(tour.getName());
        oldTour.setDescription(tour.getDescription());
        oldTour.setPrice(tour.getPrice());
        oldTour.setImage(tour.getImage());
        return tourRepository.save(oldTour);
    }

    public Tour deleteTour(long id){
        Tour oldTour = getTourById(id);
        oldTour.setDeleted(true);
        return tourRepository.save(oldTour);
    }

    public Tour getTourById(long id){
        Tour oldTour = tourRepository.findTourById(id);
        if(oldTour == null) {
            throw new NotFoundException("Tour not found!");
        }
        return oldTour;
    }

}
