package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.KoiRequest;
import com.koitourdemo.demo.model.response.KoiPageResponse;
import com.koitourdemo.demo.model.response.KoiResponse;
import com.koitourdemo.demo.repository.KoiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class KoiService {

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public Koi createNewKoi(KoiRequest koiRequest){
//        Koi koi = modelMapper.map(koiRequest, Koi.class);

        Koi koi = new Koi();
        koi.setName(koiRequest.getName());
        koi.setFarmName(koiRequest.getFarmName());
        koi.setType(koiRequest.getType());
        koi.setSize(koiRequest.getSize());
        koi.setOrigin(koiRequest.getOrigin());
        koi.setPrice(koiRequest.getPrice());
        koi.setImage(koiRequest.getImage());
        koi.setCreateAt(new Date());

        User userRequest = authenticationService.getCurrentUser();
        koi.setUser(userRequest);
        
        try {
            Koi newKoi = koiRepository.save(koi);
            return newKoi;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("error when saving koi to db");
        }
    }

    public KoiPageResponse getAllKoi(int page, int size){
        // Lấy tất cả student tồn tại trong DB
        Page koiPage = koiRepository.findAll(PageRequest.of(page, size));
        KoiPageResponse koiResponse = new KoiPageResponse();
        koiResponse.setTotalPages(koiPage.getTotalPages());
        koiResponse.setContent(koiPage.getContent());
        koiResponse.setPageNumber(koiPage.getNumber());
        koiResponse.setTotalElements(koiPage.getTotalElements());
        return koiResponse;
    }

    public KoiResponse updateKoi(KoiRequest koiRequest, UUID id){
        Koi oldKoi = getKoiById(id);
        oldKoi.setName(koiRequest.getName());
        oldKoi.setFarmName(koiRequest.getFarmName());
        oldKoi.setSize(koiRequest.getSize());
        oldKoi.setOrigin(koiRequest.getOrigin());
        oldKoi.setPrice(koiRequest.getPrice());
        oldKoi.setImage(koiRequest.getImage());
        Koi updatedKoi = koiRepository.save(oldKoi);
        return convertToKoiResponse(updatedKoi);
    }

    private KoiResponse convertToKoiResponse(Koi koi) {
        KoiResponse response = new KoiResponse();
        response.setName(koi.getName());
        response.setFarmName(koi.getFarmName());
        response.setType(koi.getType());
        response.setSize(koi.getSize());
        response.setOrigin(koi.getOrigin());
        response.setPrice(koi.getPrice());
        response.setImage(koi.getImage());
        return response;
    }

    public Koi deleteKoi(UUID id){
        Koi oldKoi = getKoiById(id);
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }

    public Koi getKoiById(UUID id){
        Koi oldKoi = koiRepository.findKoiById(id);
        if(oldKoi == null) {
            throw new NotFoundException("Koi not found!");
        }
        return oldKoi;
    }

}