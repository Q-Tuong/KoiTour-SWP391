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
        Koi newKoi = koiRepository.save(koi);
        return newKoi;
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

    public KoiResponse updateKoi(KoiRequest koiRequest, UUID id) {
        Koi oldKoi = koiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy cá Koi này!"));
        oldKoi.setName(koiRequest.getName());
        oldKoi.setFarmName(koiRequest.getFarmName());
        oldKoi.setSize(koiRequest.getSize());
        oldKoi.setOrigin(koiRequest.getOrigin());
        oldKoi.setPrice(koiRequest.getPrice());
        oldKoi.setImage(koiRequest.getImage());
        Koi updatedKoi = koiRepository.save(oldKoi);
        return modelMapper.map(updatedKoi, KoiResponse.class);
    }

    public KoiResponse deleteKoi(UUID id) {
        Koi oldKoi = koiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy cá Koi này!"));
        oldKoi.setDeleted(true);
        Koi deletedKoi = koiRepository.save(oldKoi);
        return modelMapper.map(deletedKoi, KoiResponse.class);
    }

    public KoiResponse getKoiById(UUID id) {
        Koi koi = koiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy cá Koi này!"));
        return modelMapper.map(koi, KoiResponse.class);
    }

    public Koi getKoiEntityById(UUID id) {
        return koiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không thể tìm thấy cá Koi này!"));
    }

}