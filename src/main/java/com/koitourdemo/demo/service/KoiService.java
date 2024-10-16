package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.KoiRequest;
import com.koitourdemo.demo.repository.KoiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Koi koi = modelMapper.map(koiRequest, Koi.class);
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

    public List<Koi> getAllKoi(){
        List<Koi> kois = koiRepository.findKoisByIsDeletedFalse();
        return kois;
    }

    public Koi updateKoi(Koi koi, UUID id){
        Koi oldKoi = getKoiById(id);
        oldKoi.setName(koi.getName());
        oldKoi.setSize(koi.getSize());
        oldKoi.setWeight(koi.getWeight());
        oldKoi.setColor(koi.getColor());
        oldKoi.setDescription(koi.getDescription());
        oldKoi.setPrice(koi.getPrice());
        oldKoi.setOrigin(koi.getOrigin());
        oldKoi.setImage(koi.getImage());
        return koiRepository.save(oldKoi);
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