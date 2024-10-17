package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Orders;
import com.koitourdemo.demo.model.request.OrderRequest;
import com.koitourdemo.demo.service.AuthenticationService;
import com.koitourdemo.demo.service.OrderService;
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
    OrderService orderService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody OrderRequest orderRequest) throws Exception {
        String vnPayUrl = orderService.createUrl(orderRequest);
        return ResponseEntity.ok(vnPayUrl);
    }

    @PostMapping("transactions")
    public ResponseEntity create(@RequestParam UUID orderId) throws Exception {
        orderService.createNewTransactions(orderId);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllOrder(){
        List<Orders> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }
}
