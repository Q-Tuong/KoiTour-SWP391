package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.*;
import com.koitourdemo.demo.enums.OrderStatus;
import com.koitourdemo.demo.enums.PaymentEnums;
import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.enums.TransactionsEnum;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.exception.IllegalStateException;
import com.koitourdemo.demo.model.EmailDetail;
import com.koitourdemo.demo.model.request.KoiOrderDetailRequest;
import com.koitourdemo.demo.model.request.KoiOrderRequest;
import com.koitourdemo.demo.model.response.KoiOrderDetailResponse;
import com.koitourdemo.demo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
public class KoiOrderService {

    private static final Logger log = LoggerFactory.getLogger(KoiOrderService.class);

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmailService emailService;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    KoiOrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Transactional
    public KoiOrder createKoiOrder(KoiOrderRequest orderRequest) {
        User customer = authenticationService.getCurrentUser();

        // Tạo order chính
        KoiOrder order = new KoiOrder();
        order.setCustomer(customer);
        order.setCustomerEmail(customer.getEmail());
        order.setCreateAt(new Date());
        order.setStatus(OrderStatus.PENDING);

        // Tạo danh sách order details và tính tổng tiền
        List<KoiOrderDetail> orderDetails = new ArrayList<>();
        float orderTotalPrice = 0;

        for (KoiOrderDetailRequest detail : orderRequest.getDetails()) {
            // Lấy thông tin cá
            Koi koi = koiRepository.findById(detail.getKoiId())
                    .orElseThrow(() -> new NotFoundException("Koi not found with id: " + detail.getKoiId()));

            // Validate số lượng
            if (detail.getQuantity() <= 0) {
                throw new NotFoundException("Invalid quantity for koi: " + koi.getId());
            }

            // Lấy giá từ Koi và tính toán
            float unitPrice = koi.getPrice(); // Lấy giá từ Koi
            float detailTotalPrice = unitPrice * detail.getQuantity(); // Tính tổng tiền cho detail này

            // Tạo order detail cho từng con cá
            KoiOrderDetail orderDetail = new KoiOrderDetail();
            orderDetail.setKoi(koi);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitPrice(unitPrice); // Set đơn giá lấy từ Koi
            orderDetail.setTotalPrice(detailTotalPrice); // Set tổng tiền đã tính
            orderDetail.setProductName(String.format("%s - %s", koi.getName(), koi.getOrigin()));

            orderDetails.add(orderDetail);
            orderTotalPrice += detailTotalPrice; // Cộng vào tổng tiền của order
        }

        order.setTotal(orderTotalPrice); // Set tổng tiền cho order
        order.setKoiOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public List<KoiOrder> getAllOrder(){
        User user = authenticationService.getCurrentUser();
        List<KoiOrder> orders = orderRepository.findKoiOrderByCustomer(user);
        return orders;
    }

    public List<KoiOrderDetailResponse> getOrderDetails(UUID orderId) {
        KoiOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        List<KoiOrderDetailResponse> responseList = new ArrayList<>();

        for (KoiOrderDetail detail : order.getKoiOrderDetails()) {
            KoiOrderDetailResponse response = new KoiOrderDetailResponse();
            response.setProductName(detail.getProductName());
            response.setQuantity(detail.getQuantity());
            response.setUnitPrice(detail.getUnitPrice());
            response.setTotalPrice(detail.getUnitPrice() * detail.getQuantity());

            if (detail.getKoi() != null) {
                response.setImgUrl(detail.getKoi().getImgUrl());
            }
            responseList.add(response);
        }
        return responseList;
    }

    public String createUrl(KoiOrderRequest orderRequest) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        // code của mình
        // tạo order
        KoiOrder orders = createKoiOrder(orderRequest);

        float money = orders.getTotal() * 100;
        String amount = String.valueOf((long) money);

        String tmnCode = "0731HE82";
        String secretKey = "506GUHNO9MTI5Q23PQAUCLTHOWSF3FAM";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:3000/kois/transaction?orderID=" + orders.getId();
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
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

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
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

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
            List<KoiOrder> timeoutOrders = orderRepository.findTimeoutOrders(OrderStatus.PENDING, checkDate);
            for (KoiOrder order : timeoutOrders) {
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

    // Thêm method tìm order theo email cho sale staff
    public List<KoiOrder> findOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public List<KoiOrder> getPaidOrders() {
        return orderRepository.findByStatus(OrderStatus.PAID);
    }

    public String regeneratePaymentLink(UUID orderId) throws Exception {
        KoiOrder order = orderRepository.findById(orderId)
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
        String returnUrl = "http://localhost:3000/kois/transaction?orderID=" + order.getId();
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

    public void createNewKoiTransactions(UUID uuid){
        KoiOrder orders = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        List<Map<String, Object>> orderDetails = new ArrayList<>();
        for (KoiOrderDetail detail : orders.getKoiOrderDetails()) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("productName", detail.getProductName());
            detailMap.put("quantity", detail.getQuantity());
            detailMap.put("unitPrice", detail.getUnitPrice());
            detailMap.put("totalPrice", detail.getUnitPrice() * detail.getQuantity());
            orderDetails.add(detailMap);
        }

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setReceiver(orders.getCustomer());
        emailDetail.setSubject("Xác nhận đơn hàng từ LoyaltyKoi.vn");
        emailDetail.setOrderId(orders.getId().toString());
        emailDetail.setCreateAt(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(orders.getCreateAt()));
        emailDetail.setTotalPrice(String.format("%,.0f", orders.getTotal()));
        emailDetail.setOrderDetails(orderDetails);

        // Gửi email
        emailService.sendKoiBillEmail(emailDetail);

        // tạo payment
        Payment payment = new Payment();
        payment.setKoiOrder(orders);
        payment.setCreateAt(new Date());
        payment.setTotal(orders.getTotal());
        payment.setPaymentMethod(PaymentEnums.BANKING);

        Set<KoiTransaction> setTransactions = new HashSet<>();

        // tạo transaction
        KoiTransaction transactions1 = new KoiTransaction();
        // VNPay to customer
        User customer = authenticationService.getCurrentUser();
        transactions1.setCreateAt(new Date());
        transactions1.setFrom(null);
        transactions1.setTo(customer);
        transactions1.setKoiPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("NAP TIEN VNPAY TO CUSTOMER");
        setTransactions.add(transactions1);

        KoiTransaction transactions2 = new KoiTransaction();
        // CUSTOMER TO ADMIN
        User admin = userRepository.findUserByRole(Role.ADMIN);
        transactions2.setCreateAt(new Date());
        transactions2.setFrom(customer);
        transactions2.setTo(admin);
        transactions2.setKoiPayment(payment);
        transactions2.setStatus(TransactionsEnum.SUCCESS);
        transactions2.setDescription("CUSTOMER TO ADMIN");

        float newBalance = admin.getKoiBalance() + orders.getTotal();
        transactions2.setAmount(orders.getTotal());
        admin.setKoiBalance(newBalance);
        setTransactions.add(transactions2);

        payment.setKoiTransactions(setTransactions);
        orders.setStatus(OrderStatus.PAID);
        userRepository.save(admin);
        paymentRepository.save(payment);
    }

    public KoiOrder completeOrder(UUID orderId) {
        KoiOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Kiểm tra xem order có payment chưa
        List<Payment> payments = paymentRepository.findByKoiOrder(order);
        if (payments.isEmpty()) {
            throw new IllegalStateException("Cannot complete unpaid order");
        }

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be in PENDING state to complete");
        }

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    public KoiOrder cancelOrder(UUID orderId) {
        KoiOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Kiểm tra xem order có payment chưa
        List<Payment> payments = paymentRepository.findByKoiOrder(order);
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
