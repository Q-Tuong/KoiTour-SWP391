package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.model.request.KoiRequest;
import com.koitourdemo.demo.model.response.KoiPageResponse;
import com.koitourdemo.demo.service.KoiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PreAuthorize("hasAuthority('CONSULTING_STAFF')")
@RequestMapping("/api/koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @PostMapping("/create")
    public ResponseEntity createKoi(@Valid @RequestBody KoiRequest koi){
        Koi newKoi = koiService.createNewKoi(koi);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping("/get-all")
    public ResponseEntity getKoiList(@RequestParam int page, @RequestParam(defaultValue = "5") int size){
        KoiPageResponse koiResponse = koiService.getAllKoi(page, size);
        return ResponseEntity.ok(koiResponse);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity updateKoi(@Valid @RequestBody Koi koi, @PathVariable UUID id){
        Koi updated = koiService.updateKoi(koi, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteKoi(@PathVariable UUID id){
        Koi deleted = koiService.deleteKoi(id);
        return ResponseEntity.ok(deleted);
    }
}
