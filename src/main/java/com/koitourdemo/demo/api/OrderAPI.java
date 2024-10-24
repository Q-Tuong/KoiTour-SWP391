package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.KoiOrder;
import com.koitourdemo.demo.entity.TourOrder;
import com.koitourdemo.demo.model.request.KoiOrderRequest;
import com.koitourdemo.demo.model.request.TourOrderRequest;
import com.koitourdemo.demo.service.AuthenticationService;
import com.koitourdemo.demo.service.KoiOrderService;
import com.koitourdemo.demo.service.TourOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    KoiOrderService koiOrderService;

    @Autowired
    TourOrderService tourOrderService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/create/koi")
    public ResponseEntity<?> createNewKoiOrder(@RequestBody KoiOrderRequest orderRequest) {
        try {
            String vnPayUrl = koiOrderService.createUrl(orderRequest);
            return ResponseEntity.ok(vnPayUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("transactions/koi")
    public ResponseEntity createKoiTransaction(@RequestParam UUID orderId) throws Exception {
        koiOrderService.createNewKoiTransactions(orderId);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get-all/koi")
    public ResponseEntity getAllKoiOrder(){
        List<KoiOrder> orders = koiOrderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create/tour")
    public ResponseEntity<?> createNewTourOrder(@RequestBody TourOrderRequest tourOrderRequest) {
        try {
            String vnPayUrl = tourOrderService.createUrl(tourOrderRequest);
            return ResponseEntity.ok(vnPayUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("transactions/tour")
    public ResponseEntity createNewTourTransaction(@RequestParam UUID orderId) throws Exception {
        tourOrderService.createNewTourTransactions(orderId);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get-all/tour")
    public ResponseEntity getAllTourOrder() {
        List<TourOrder> orders = tourOrderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }
}
