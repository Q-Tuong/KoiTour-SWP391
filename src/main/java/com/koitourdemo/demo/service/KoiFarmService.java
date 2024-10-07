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

    public KoiFarm updateKoiFarm(KoiFarm koiFarm, long koiFarmId){
        KoiFarm oldKoiFarm = getKoiFarmByKoiFarmId(koiFarmId);
        oldKoiFarm.setKoiFarmName(koiFarm.getKoiFarmName());
        oldKoiFarm.setKoiFarmAddress(koiFarm.getKoiFarmAddress());
        oldKoiFarm.setKoiFarmPhone(koiFarm.getKoiFarmPhone());
        oldKoiFarm.setKoiFarmDescription(koiFarm.getKoiFarmDescription());
        return koiFarmRepository.save(oldKoiFarm);
    }

    public KoiFarm deleteKoiFarm(long koiFarmId){
        KoiFarm oldKoiFarm = getKoiFarmByKoiFarmId(koiFarmId);
        oldKoiFarm.setDeleted(true);
        return koiFarmRepository.save(oldKoiFarm);
    }

    public KoiFarm getKoiFarmByKoiFarmId(long koiFarmId){
        KoiFarm oldKoiFarm = koiFarmRepository.findKoiFarmByKoiFarmId(koiFarmId);
        if(oldKoiFarm == null)
            throw new NotFoundException("Koi not found!");
        return oldKoiFarm;
    }
}
