package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.KoiFarm;
import com.koitourdemo.demo.model.KoiFarmRequest;
import com.koitourdemo.demo.service.KoiFarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("api/koiFarm")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFarmAPI {

    @Autowired
    KoiFarmService koiFarmService;

    @PostMapping
    public ResponseEntity createKoiFarm(@Valid @RequestBody KoiFarmRequest koiFarm){
        KoiFarm newKoiFarm = koiFarmService.createNewKoiFarm(koiFarm);
        return ResponseEntity.ok(newKoiFarm);
    }

    @GetMapping
    public ResponseEntity getAllKoiFarm(){
        List<KoiFarm> koiFarms = koiFarmService.getAllKoiFarm();
        return ResponseEntity.ok(koiFarms);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoi(@Valid @RequestBody KoiFarm koiFarm, @PathVariable long id){
        KoiFarm updateKoiFarm = koiFarmService.updateKoiFarm(koiFarm, id);
        return ResponseEntity.ok(updateKoiFarm);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoiFarm(@PathVariable long id){
        KoiFarm deleteKoiFarm = koiFarmService.deleteKoiFarm(id);
        return ResponseEntity.ok(deleteKoiFarm);
    }

}
