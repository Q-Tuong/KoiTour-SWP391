package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Tour;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.TourRequest;
import com.koitourdemo.demo.model.response.TourPageResponse;
import com.koitourdemo.demo.model.response.TourResponse;
import com.koitourdemo.demo.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public Tour createNewTour(TourRequest tourRequest){
        Tour tour = new Tour();
        tour.setCode(tourRequest.getCode());
        tour.setName(tourRequest.getName());
        tour.setDuration(tourRequest.getDuration());
        tour.setStartAt(tourRequest.getStartAt());
        tour.setDescription(tourRequest.getDescription());
        tour.setPrice(tourRequest.getPrice());
        tour.setImage(tourRequest.getImage());
        tour.setCreateAt(new Date());

        User userRequest = authenticationService.getCurrentUser();
        tour.setUser(userRequest);
        Tour newTour = tourRepository.save(tour);
        return newTour;
    }

    public TourPageResponse getAllTour(int page, int size){
        Page tourPage = tourRepository.findAll(PageRequest.of(page, size));
        TourPageResponse tourResponse = new TourPageResponse();
        tourResponse.setTotalPages(tourPage.getTotalPages());
        tourResponse.setContent(tourPage.getContent());
        tourResponse.setPageNumber(tourPage.getNumber());
        tourResponse.setTotalElements(tourPage.getTotalElements());
        return tourResponse;
    }

    public TourResponse updateTour(TourRequest tourRequest, long id) {
        Tour oldTour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found!"));
        oldTour.setName(tourRequest.getName());
        oldTour.setDuration(tourRequest.getDuration());
        oldTour.setStartAt(tourRequest.getStartAt());
        oldTour.setDescription(tourRequest.getDescription());
        oldTour.setPrice(tourRequest.getPrice());
        oldTour.setImage(tourRequest.getImage());
        Tour updatedTour = tourRepository.save(oldTour);
        return modelMapper.map(updatedTour, TourResponse.class);
    }

    public TourResponse deleteTour(long id) {
        Tour oldTour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found!"));
        oldTour.setDeleted(true);
        Tour deletedTour = tourRepository.save(oldTour);
        return modelMapper.map(deletedTour, TourResponse.class);
    }

    public TourResponse getTourById(long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found!"));
        return modelMapper.map(tour, TourResponse.class);
    }

}
