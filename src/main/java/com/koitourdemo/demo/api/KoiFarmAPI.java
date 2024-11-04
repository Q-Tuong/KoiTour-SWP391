package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.KoiFarm;
import com.koitourdemo.demo.model.request.KoiFarmRequest;
import com.koitourdemo.demo.model.response.KoiFarmPageResponse;
import com.koitourdemo.demo.model.response.KoiFarmResponse;
import com.koitourdemo.demo.service.KoiFarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/koiFarm")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFarmAPI {

    @Autowired
    KoiFarmService koiFarmService;

    @PostMapping("/create")
    public ResponseEntity createKoiFarm(@Valid @RequestBody KoiFarmRequest koiFarm){
        KoiFarm newKoiFarm = koiFarmService.createNewKoiFarm(koiFarm);
        return ResponseEntity.ok(newKoiFarm);
    }

    @GetMapping("/search")
    public ResponseEntity<KoiFarmPageResponse> searchKoiFarm(@RequestParam(required = false) String keyword,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        KoiFarmPageResponse koiFarmPageResponse = koiFarmService.searchKoiFarm(keyword, page, size);
        return ResponseEntity.ok(koiFarmPageResponse);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllKoiFarm(@RequestParam int page, @RequestParam(defaultValue = "5") int size){
        KoiFarmPageResponse koiFarmPageResponse = koiFarmService.getAllKoiFarm(page, size);
        return ResponseEntity.ok(koiFarmPageResponse);
    }

    @GetMapping("/get-by-id/{koiFarmId}")
    public ResponseEntity<KoiFarmResponse> getKoiFarmById(@PathVariable long koiFarmId) {
        KoiFarmResponse koiFarmResponse = koiFarmService.getKoiFarmById(koiFarmId);
        return ResponseEntity.ok(koiFarmResponse);
    }

    @PutMapping("/update/{koiFarmId}")
    public ResponseEntity updateKoi(@Valid @RequestBody KoiFarmRequest koiFarmRequest, @PathVariable long koiFarmId){
        KoiFarmResponse updated = koiFarmService.updateKoiFarm(koiFarmRequest, koiFarmId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{koiFarmId}")
    public ResponseEntity deleteKoiFarm(@PathVariable long koiFarmId){
        KoiFarmResponse deleted = koiFarmService.deleteKoiFarm(koiFarmId);
        return ResponseEntity.ok(deleted);
    }

}
