package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.model.KoiRequest;
import com.koitourdemo.demo.service.KoiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/koi")
@CrossOrigin("*")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @PostMapping
    @PreAuthorize("STAFF")
    public ResponseEntity createKoi(@Valid @RequestBody KoiRequest koi){
        Koi newKoi = koiService.createNewKoi(koi);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping
    public ResponseEntity get(){
        List<Koi> kois = koiService.getAllKoi();
        return ResponseEntity.ok(kois);
    }

    @PutMapping("{koiId}")
    public ResponseEntity updateKoi(@Valid @RequestBody Koi koi, @PathVariable long koiId){
        Koi updateKoi = koiService.updateKoi(koi, koiId);
        return ResponseEntity.ok(updateKoi);
    }

    @DeleteMapping("{koiId}")
    public ResponseEntity deleteKoi(@PathVariable long koiId){
        Koi deleteKoi = koiService.deleteKoi(koiId);
        return ResponseEntity.ok(deleteKoi);
    }
}
