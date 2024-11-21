package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.*;
import com.koitourdemo.demo.enums.OrderStatus;
import com.koitourdemo.demo.enums.PaymentEnums;
import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.enums.TransactionsEnum;
import com.koitourdemo.demo.exception.IllegalStateException;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.EmailDetail;
import com.koitourdemo.demo.model.request.TourOrderRequest;
import com.koitourdemo.demo.model.request.TourOrderDetailRequest;
import com.koitourdemo.demo.model.response.TourOrderDetailResponse;
import com.koitourdemo.demo.repository.TourOrderRepository;
import com.koitourdemo.demo.repository.TourRepository;
import com.koitourdemo.demo.repository.UserRepository;
import com.koitourdemo.demo.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TourOrderService {
    private static final Logger log = LoggerFactory.getLogger(TourOrderService.class);

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmailService emailService;

    @Autowired
    TourRepository tourRepository;

    @Autowired
    TourOrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public TourOrder createTourOrder(TourOrderRequest orderRequest) {
        User customer = authenticationService.getCurrentUser();

        TourOrder order = new TourOrder();
        order.setCustomer(customer);
        order.setCreateAt(new Date());
        order.setStatus(OrderStatus.PENDING);

        List<TourOrderDetail> orderDetails = new ArrayList<>();
        float orderTotal = 0;

        for (TourOrderDetailRequest detail : orderRequest.getDetails()) {
            Tour tour = tourRepository.findById(detail.getTourId())
                    .orElseThrow(() -> new NotFoundException("Tour not found with id: " + detail.getTourId()));

            if (detail.getQuantity() <= 0) {
                throw new NotFoundException("Invalid quantity for tour: " + tour.getId());
            }

            TourOrderDetail orderDetail = new TourOrderDetail();
            orderDetail.setTour(tour);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitPrice(detail.getUnitPrice());
            orderDetail.setTotalPrice(detail.getTotalPrice());
            orderDetail.setTourName(tour.getName());
            orderDetail.setDuration(tour.getDuration());
            orderDetail.setStartAt(tour.getStartAt());

            orderDetails.add(orderDetail);
            orderTotal += detail.getTotalPrice();
        }

        order.setTotal(orderTotal);
        order.setTourOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public List<TourOrder> getAllOrder() {
        User user = authenticationService.getCurrentUser();
        List<TourOrder> orders = orderRepository.findTourOderByCustomer(user);
        return orders;
    }

    public List<TourOrderDetailResponse> getOrderDetails(UUID orderId) {
        TourOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        List<TourOrderDetailResponse> responseList = new ArrayList<>();

        for (TourOrderDetail detail : order.getTourOrderDetails()) {
            TourOrderDetailResponse response = new TourOrderDetailResponse();
            response.setProductName(detail.getTourName());
            response.setQuantity(detail.getQuantity());
            response.setUnitPrice(detail.getUnitPrice());
            response.setTotalPrice(detail.getUnitPrice() * detail.getQuantity());

            if (detail.getTour() != null) {
                response.setImgUrl(detail.getTour().getImgUrl());
            }

            responseList.add(response);
        }
        return responseList;
    }

    public String createUrl(TourOrderRequest orderRequest) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        // Sử dụng createTourOrder có sẵn
        TourOrder orders = createTourOrder(orderRequest);

        float money = orders.getTotal() * 100;
        String amount = String.valueOf((long) money);

        String tmnCode = "0731HE82";
        String secretKey = "506GUHNO9MTI5Q23PQAUCLTHOWSF3FAM";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:3000/tours/transaction?orderID=" + orders.getId();
        String currCode = "VND";
        UUID orderId = orders.getId();
        String txnRef = orderId.toString();

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + orders.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", amount);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1);

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);
        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        return urlBuilder.toString();
    }

    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    @Scheduled(fixedRate = 1000 * 60 * 8)
    public void checkPendingOrders() {
        try {
            LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
            Date checkDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
            List<TourOrder> timeoutOrders = orderRepository.findTimeoutOrders(OrderStatus.PENDING, checkDate); // Sửa KoiOrder thành TourOrder
            for (TourOrder order : timeoutOrders) {
                try {
                    order.setStatus(OrderStatus.EXPIRED);
                    orderRepository.save(order);
                    log.info("Canceled timeout order: {}", order.getId());
                } catch (Exception e) {
                    log.error("Error canceling order {}: {}", order.getId(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in checkPendingOrders: {}", e.getMessage(), e);
        }
    }

    public List<TourOrder> findOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public List<TourOrder> getPaidOrders() {
        return orderRepository.findByStatus(OrderStatus.PAID);
    }

    public String regeneratePaymentLink(UUID orderId) throws Exception {
        TourOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.EXPIRED) {
            throw new NotFoundException("Order is not in EXPIRED state");
        }

        // Reset createAt time
        order.setCreateAt(new Date());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        // Tạo URL trực tiếp thay vì gọi createUrl
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        float money = order.getTotal() * 100;
        String amount = String.valueOf((long) money);

        String tmnCode = "0731HE82";
        String secretKey = "506GUHNO9MTI5Q23PQAUCLTHOWSF3FAM";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:3000/tours/transaction?orderID=" + order.getId();
        String currCode = "VND";
        String txnRef = order.getId().toString();

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + order.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", amount);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1);

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);
        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        return urlBuilder.toString();
    }

    public void createNewTourTransactions(UUID uuid) {
        TourOrder orders = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        List<Map<String, Object>> orderDetails = new ArrayList<>();
        for (TourOrderDetail detail : orders.getTourOrderDetails()) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("tourName", detail.getTourName());
            detailMap.put("quantity", detail.getQuantity());
            detailMap.put("unitPrice", String.format("%,.0f", detail.getUnitPrice()));
            detailMap.put("totalPrice", String.format("%,.0f", detail.getTotalPrice()));
            detailMap.put("duration", detail.getDuration());
            detailMap.put("startAt", detail.getStartAt());
            detailMap.put("startFrom", detail.getTour().getStartFrom());
            orderDetails.add(detailMap);
        }

        // Chuẩn bị email detail
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setReceiver(orders.getCustomer());
        emailDetail.setSubject("Xác nhận đơn hàng Tour từ LoyaltyKoi.vn");
        emailDetail.setOrderId(orders.getId().toString());
        emailDetail.setCreateAt(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(orders.getCreateAt()));
        emailDetail.setTotalPrice(String.format("%,.0f", orders.getTotal()));
        emailDetail.setOrderDetails(orderDetails);

        // Gửi email
        emailService.sendTourBillEmail(emailDetail);

        Payment payment = new Payment();
        payment.setTourOrder(orders);
        payment.setCreateAt(new Date());
        payment.setTotal(orders.getTotal());
        payment.setPaymentMethod(PaymentEnums.BANKING);

        Set<TourTransaction> setTransactions = new HashSet<>();

        // VNPay to customer
        TourTransaction transactions1 = new TourTransaction();
        User customer = authenticationService.getCurrentUser();
        transactions1.setCreateAt(new Date());
        transactions1.setFrom(null);
        transactions1.setTo(customer);
        transactions1.setTourPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("NAP TIEN VNPAY TO CUSTOMER");
        setTransactions.add(transactions1);

        // CUSTOMER TO ADMIN
        TourTransaction transactions2 = new TourTransaction();
        User admin = userRepository.findUserByRole(Role.ADMIN);
        transactions2.setCreateAt(new Date());
        transactions2.setFrom(customer);
        transactions2.setTo(admin);
        transactions2.setTourPayment(payment);
        transactions2.setStatus(TransactionsEnum.SUCCESS);
        transactions2.setDescription("CUSTOMER TO ADMIN");

        float newBalance = admin.getTourBalance() + orders.getTotal();
        transactions2.setAmount(orders.getTotal());
        admin.setTourBalance(newBalance);
        setTransactions.add(transactions2);

        payment.setTourTransactions(setTransactions);
        orders.setStatus(OrderStatus.PAID);
        userRepository.save(admin);
        paymentRepository.save(payment);
    }

    public TourOrder completeOrder(UUID orderId) {
        TourOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        List<Payment> payments = paymentRepository.findByTourOrder(order);

        if (payments.isEmpty()) {
            throw new IllegalStateException("Cannot complete unpaid order");
        }
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be in PENDING state to complete");
        }
        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    public TourOrder cancelOrder(UUID orderId) {
        TourOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        List<Payment> payments = paymentRepository.findByTourOrder(order);

        if (!payments.isEmpty()) {
            throw new IllegalStateException("Cannot cancel paid order");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only cancel PENDING orders");
        }
        order.setStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }
}
