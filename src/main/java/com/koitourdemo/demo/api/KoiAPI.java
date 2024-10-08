package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.model.KoiRequest;
import com.koitourdemo.demo.service.KoiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasAuthority('CONSULTING_STAFF')")
@RequestMapping("/api/koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @PostMapping
    public ResponseEntity createKoi(@Valid @RequestBody KoiRequest koi){
        Koi newKoi = koiService.createNewKoi(koi);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping
    public ResponseEntity getAllKoi(){
        List<Koi> kois = koiService.getAllKoi();
        return ResponseEntity.ok(kois);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoi(@Valid @RequestBody Koi koi, @PathVariable UUID id){
        Koi updateKoi = koiService.updateKoi(koi, id);
        return ResponseEntity.ok(updateKoi);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable UUID id){
        Koi deleteKoi = koiService.deleteKoi(id);
        return ResponseEntity.ok(deleteKoi);
    }
}
