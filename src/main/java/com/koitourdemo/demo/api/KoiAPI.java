package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.model.ApiResponse;
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createKoi(@Valid @RequestBody KoiRequest koi){
        Koi newKoi = koiService.createNewKoi(koi);
        return ResponseEntity.ok(new ApiResponse("Create new Koi successfully!", newKoi));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllKoi(){
        List<Koi> kois = koiService.getAllKoi();
        return ResponseEntity.ok(new ApiResponse("Successfully!", kois));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateKoi(@Valid @RequestBody Koi koi, @PathVariable UUID id){
        Koi updated = koiService.updateKoi(koi, id);
        return ResponseEntity.ok(new ApiResponse("Update Koi information successfully!", updated));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteKoi(@PathVariable UUID id){
        Koi deleted = koiService.deleteKoi(id);
        return ResponseEntity.ok(new ApiResponse("Delete koi successfully!", deleted));
    }
}
