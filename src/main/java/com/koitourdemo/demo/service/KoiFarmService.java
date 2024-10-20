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
//        KoiFarm koiFarm = modelMapper.map(koiFarmRequest, KoiFarm.class);
        KoiFarm koiFarm = new KoiFarm();
        koiFarm.setName(koiFarmRequest.getName());
        koiFarm.setAddress(koiFarmRequest.getAddress());
        koiFarm.setPhone(koiFarmRequest.getPhone());
        koiFarm.setDescription(koiFarmRequest.getDescription());
        koiFarm.setImage(koiFarmRequest.getImage());
        koiFarm.setCreateAt(new Date());

        User userRequest = authenticationService.getCurrentUser();
        koiFarm.setConsulting(userRequest);

        try{
            KoiFarm newKoiFarm = koiFarmRepository.save(koiFarm);
            return newKoiFarm;
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("error when saving koi farm to db!");
        }
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
        KoiFarm oldKoiFarm = getKoiFarmById(id);
        oldKoiFarm.setName(koiFarmRequest.getName());
        oldKoiFarm.setAddress(koiFarmRequest.getAddress());
        oldKoiFarm.setPhone(koiFarmRequest.getPhone());
        oldKoiFarm.setDescription(koiFarmRequest.getDescription());
        oldKoiFarm.setImage(koiFarmRequest.getImage());
        KoiFarm updatedKoiFarm = koiFarmRepository.save(oldKoiFarm);
        return convertToKoiFarmResponse(updatedKoiFarm);
    }

    private KoiFarmResponse convertToKoiFarmResponse(KoiFarm koiFarm) {
        KoiFarmResponse response = new KoiFarmResponse();
        response.setName(koiFarm.getName());
        response.setAddress(koiFarm.getAddress());
        response.setAddress(koiFarm.getAddress());
        response.setPhone(koiFarm.getPhone());
        response.setDescription(koiFarm.getDescription());
        response.setImage(koiFarm.getImage());
        return response;
    }

    public KoiFarm deleteKoiFarm(long id){
        KoiFarm oldKoiFarm = getKoiFarmById(id);
        oldKoiFarm.setDeleted(true);
        return koiFarmRepository.save(oldKoiFarm);
    }

    public KoiFarm getKoiFarmById(long id){
        KoiFarm oldKoiFarm = koiFarmRepository.findKoiFarmById(id);
        if(oldKoiFarm == null) {
            throw new NotFoundException("Koi not found!");
        }
        return oldKoiFarm;
    }

}
