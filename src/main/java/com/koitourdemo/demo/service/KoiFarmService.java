package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.KoiFarm;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.KoiFarmRequest;
import com.koitourdemo.demo.model.response.KoiFarmPageResponse;
import com.koitourdemo.demo.model.response.KoiFarmResponse;
import com.koitourdemo.demo.repository.KoiFarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KoiFarmService {

    @Autowired
    KoiFarmRepository koiFarmRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public KoiFarm createNewKoiFarm(KoiFarmRequest koiFarmRequest){
        KoiFarm koiFarm = new KoiFarm();
        koiFarm.setName(koiFarmRequest.getName());
        koiFarm.setAddress(koiFarmRequest.getAddress());
        koiFarm.setPhone(koiFarmRequest.getPhone());
        koiFarm.setDescription(koiFarmRequest.getDescription());
        koiFarm.setImage(koiFarmRequest.getImage());
        koiFarm.setCreateAt(new Date());

        User userRequest = authenticationService.getCurrentUser();
        koiFarm.setManager(userRequest);
        KoiFarm newKoiFarm = koiFarmRepository.save(koiFarm);
        return newKoiFarm;
    }

    public KoiFarmPageResponse getAllKoiFarm(int page, int size){
        Page koiFarmPage = koiFarmRepository.findAll(PageRequest.of(page, size));
        KoiFarmPageResponse koiFarmResponse = new KoiFarmPageResponse();
        koiFarmResponse.setTotalPages(koiFarmPage.getTotalPages());
        koiFarmResponse.setContent(koiFarmPage.getContent());
        koiFarmResponse.setPageNumber(koiFarmPage.getNumber());
        koiFarmResponse.setTotalElements(koiFarmPage.getTotalElements());
        return koiFarmResponse;
    }

    public KoiFarmResponse updateKoiFarm(KoiFarmRequest koiFarmRequest, long id){
        KoiFarm koiFarm = koiFarmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy trại cá này!"));
        koiFarm.setName(koiFarmRequest.getName());
        koiFarm.setAddress(koiFarmRequest.getAddress());
        koiFarm.setPhone(koiFarmRequest.getPhone());
        koiFarm.setDescription(koiFarmRequest.getDescription());
        koiFarm.setImage(koiFarmRequest.getImage());
        KoiFarm updatedKoiFarm = koiFarmRepository.save(koiFarm);
        return modelMapper.map(updatedKoiFarm, KoiFarmResponse.class);
    }

    public KoiFarmResponse deleteKoiFarm(long id) {
        KoiFarm oldKoiFarm = koiFarmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Koi Farm not found!"));
        oldKoiFarm.setDeleted(true);
        KoiFarm deletedKoiFarm = koiFarmRepository.save(oldKoiFarm);
        return modelMapper.map(deletedKoiFarm, KoiFarmResponse.class);
    }

    public KoiFarmResponse getKoiFarmById(long id){
        KoiFarm koiFarm = koiFarmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy trại cá này!"));
        return modelMapper.map(koiFarm, KoiFarmResponse.class);
    }

}
