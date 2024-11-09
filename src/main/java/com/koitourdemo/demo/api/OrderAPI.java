package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.KoiOrder;
import com.koitourdemo.demo.entity.TourOrder;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.KoiOrderRequest;
import com.koitourdemo.demo.model.request.TourOrderRequest;
import com.koitourdemo.demo.model.response.KoiOrderDetailResponse;
import com.koitourdemo.demo.model.response.TourOrderDetailResponse;
import com.koitourdemo.demo.service.AuthenticationService;
import com.koitourdemo.demo.service.KoiOrderService;
import com.koitourdemo.demo.service.TourOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity createNewKoiOrder(@RequestBody KoiOrderRequest orderRequest) {
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

    @GetMapping("/search-order-by-email/koi")
    public ResponseEntity findKoiOrdersByEmail(@RequestParam String email) {
        try {
            List<KoiOrder> orders = koiOrderService.findOrdersByEmail(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error finding orders!");
        }
    }

    @GetMapping("/get-paid-order/koi")
    public ResponseEntity getPaidOrders() {
        try {
            List<KoiOrder> paidOrders = koiOrderService.getPaidOrders();
            return ResponseEntity.ok(paidOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/regenerate-payment/koi")
    public ResponseEntity regeneratePaymentLink(@RequestParam UUID orderId) {
        try {
            String newPaymentUrl = koiOrderService.regeneratePaymentLink(orderId);
            return ResponseEntity.ok(newPaymentUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error regenerating payment link: " + e.getMessage());
        }
    }

    @PutMapping("/update-to-complete/koi")
    public ResponseEntity completeKoiOrder(@RequestParam UUID orderId) {
        try {
            KoiOrder order = koiOrderService.completeOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error completing order: " + e.getMessage());
        }
    }

    @PutMapping("/update-to-cancel/koi")
    public ResponseEntity cancelKoiOrder(@RequestParam UUID orderId) {
        try {
            KoiOrder order = koiOrderService.cancelOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error canceling order: " + e.getMessage());
        }
    }

    @GetMapping("/details/koi/{orderId}")
    public ResponseEntity getKoiOrderDetails(@PathVariable UUID orderId) {
        try {
            List<KoiOrderDetailResponse> orderDetails = koiOrderService.getOrderDetails(orderId);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ArrayList<>()); // Trả về list rỗng thay vì null
            }
            return ResponseEntity.ok(orderDetails);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Order not found!");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error getting order details!");
        }
    }

    @PostMapping("/create/tour")
    public ResponseEntity createNewTourOrder(@RequestBody TourOrderRequest tourOrderRequest) {
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

    @GetMapping("/search-order-by-email/tour")
    public ResponseEntity findTourOrdersByEmail(@RequestParam String email) {
        try {
            List<TourOrder> orders = tourOrderService.findOrdersByEmail(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error finding orders: " + e.getMessage());
        }
    }

    @GetMapping("/get-paid-order/tour")
    public ResponseEntity getTourPaidOrders() {
        try {
            List<TourOrder> paidOrders = tourOrderService.getPaidOrders();
            return ResponseEntity.ok(paidOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/regenerate-payment/tour")
    public ResponseEntity regenerateTourPaymentLink(@RequestParam UUID orderId) {
        try {
            String newPaymentUrl = tourOrderService.regeneratePaymentLink(orderId);
            return ResponseEntity.ok(newPaymentUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error regenerating payment link: " + e.getMessage());
        }
    }

    @PutMapping("/update-to-complete/tour")
    public ResponseEntity completeTourOrder(@RequestParam UUID orderId) {
        try {
            TourOrder order = tourOrderService.completeOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error completing order: " + e.getMessage());
        }
    }

    @PutMapping("/update-to-cancel/tour")
    public ResponseEntity cancelTourOrder(@RequestParam UUID orderId) {
        try {
            TourOrder order = tourOrderService.cancelOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error canceling order: " + e.getMessage());
        }
    }

    @GetMapping("/details/tour/{orderId}")
    public ResponseEntity getTourOrderDetails(@PathVariable UUID orderId) {
        try {
            List<TourOrderDetailResponse> orderDetails = tourOrderService.getOrderDetails(orderId);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ArrayList<>()); // Trả về list rỗng thay vì null
            }
            return ResponseEntity.ok(orderDetails);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Order not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error getting order details: " + e.getMessage());
        }
    }
}
