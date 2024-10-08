package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.entity.KoiFarm;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.KoiFarmRequest;
import com.koitourdemo.demo.repository.KoiFarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        KoiFarm koiFarm = modelMapper.map(koiFarmRequest, KoiFarm.class);
        User userRequest = authenticationService.getCurrentUser();
        koiFarm.setUser(userRequest);

        try{
            KoiFarm newKoiFarm = koiFarmRepository.save(koiFarm);
            return newKoiFarm;
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("error when saving koi farm to db!");
        }
    }

    public List<KoiFarm> getAllKoiFarm(){
        List<KoiFarm> koiFarms = koiFarmRepository.findKoiFarmsByIsDeletedFalse();
        return koiFarms;
    }

    public KoiFarm updateKoiFarm(KoiFarm koiFarm, long id){
        KoiFarm oldKoiFarm = getKoiFarmById(id);
        oldKoiFarm.setName(koiFarm.getName());
        oldKoiFarm.setAddress(koiFarm.getAddress());
        oldKoiFarm.setPhone(koiFarm.getPhone());
        oldKoiFarm.setDescription(koiFarm.getDescription());
        oldKoiFarm.setEmail(koiFarm.getEmail());
        oldKoiFarm.setImage(koiFarm.getImage());
        return koiFarmRepository.save(oldKoiFarm);
    }

    public KoiFarm deleteKoiFarm(long id){
        KoiFarm oldKoiFarm = getKoiFarmById(id);
        oldKoiFarm.setDeleted(true);
        return koiFarmRepository.save(oldKoiFarm);
    }

    public KoiFarm getKoiFarmById(long id){
        KoiFarm oldKoiFarm = koiFarmRepository.findKoiFarmById(id);
        if(oldKoiFarm == null)
            throw new NotFoundException("Koi not found!");
        return oldKoiFarm;
    }
}
