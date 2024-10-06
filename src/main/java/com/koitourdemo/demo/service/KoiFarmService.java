package com.koitourdemo.demo.service;

import com.koitourdemo.demo.repository.KoiFarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KoiFarmService {

    @Autowired
    KoiFarmRepository koiFarmRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

//    public KoiFarm createNewKoi(KoiRequest koiRequest){
//        KoiFarm koi = modelMapper.map(koiRequest, Koi.class);
//        User userRequest = authenticationService.getCurrentUser();
//        koi.setUser(userRequest);
//
//        Koi newKoi = koiRepository.save(koi);
//        return newKoi;
//    }
//
//    public List<Koi> getAllKoi(){
//        List<Koi> kois = koiRepository.findKoisByIsDeletedFalse();
//        return kois;
//    }
//
//    public Koi updateKoi(Koi koi, long koiId){
//        Koi oldKoi = getKoiByKoiId(koiId);
//        oldKoi.setKoiName(koi.getKoiName());
//        oldKoi.setKoiSize(koi.getKoiSize());
//        oldKoi.setKoiWeight(koi.getKoiWeight());
//        oldKoi.setKoiColor(koi.getKoiColor());
//        oldKoi.setKoiDescription(koi.getKoiDescription());
//        return koiRepository.save(oldKoi);
//    }
//
//    public Koi deleteKoi(long koiId){
//        Koi oldKoi = getKoiByKoiId(koiId);
//        oldKoi.setDeleted(true);
//        return koiRepository.save(oldKoi);
//    }
//
//    public Koi getKoiByKoiId(long koiId){
//        Koi oldKoi = koiRepository.findKoiByKoiId(koiId);
//        if(oldKoi == null)
//            throw new NotFoundException("Koi not found!");
//        return oldKoi;
//    }
}
