package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Orders;
import com.koitourdemo.demo.model.OrderRequest;
import com.koitourdemo.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity create(@RequestBody OrderRequest orderRequest){
        Orders order = orderService.createNewOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity getAllOrder(){
        List<Orders> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }
}
