package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.*;
import com.koitourdemo.demo.enums.PaymentEnums;
import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.enums.TransactionsEnum;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.KoiOrderDetailRequest;
import com.koitourdemo.demo.model.request.KoiOrderRequest;
import com.koitourdemo.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class KoiOrderService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    KoiOrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public KoiOrder createKoiOrder(KoiOrderRequest orderRequest) {
        User customer = authenticationService.getCurrentUser();
        KoiOrder order = new KoiOrder();
        List<KoiOrderDetail> orderDetails = new ArrayList<>();
        order.setCustomer(customer);
        order.setCreateAt(new Date());
        float orderTotal = 0;

        // Tính tổng tiền từ các orderDetails
        for (KoiOrderDetailRequest detail : orderRequest.getDetails()) {
            Koi koi = koiRepository.findById(detail.getKoiId())
                    .orElseThrow(() -> new NotFoundException("Koi not found with id: " + detail.getKoiId()));

            // Validate price
            if (koi.getPrice() != detail.getUnitPrice()) {
                throw new NotFoundException("Price mismatch for koi: " + koi.getId());
            }

            if (detail.getQuantity() <= 0) {
                throw new NotFoundException("Invalid quantity for koi: " + koi.getId());
            }

            KoiOrderDetail orderDetail = new KoiOrderDetail();
            orderDetail.setKoi(koi);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitPrice(detail.getUnitPrice());
            orderDetail.setTotalPrice(detail.getTotalPrice());
            orderDetail.setProductName(String.format("%s - %s", koi.getName(), koi.getFarmName()));
            orderDetails.add(orderDetail);
            orderTotal += detail.getTotalPrice();
        }

        order.setTotal(orderTotal);
        order.setKoiOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public List<KoiOrder> getAllOrder(){
        User user = authenticationService.getCurrentUser();
        List<KoiOrder> orders = orderRepository.findKoiOrderByCustomer(user);
        return orders;
    }

    public String createUrl(KoiOrderRequest orderRequest) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        // code của mình
        // tạo order
        KoiOrder orders = createKoiOrder(orderRequest);

        float money = orders.getTotal() * 100;
        String amount = String.valueOf((int) money);

        String tmnCode = "0731HE82";
        String secretKey = "506GUHNO9MTI5Q23PQAUCLTHOWSF3FAM";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "https://blearning.vn/guide/swp/docker-local?orderID=" + orders.getId();
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

    public void createNewKoiTransactions(UUID uuid){
        KoiOrder orders = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

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
        transactions2.setAmount(newBalance);
        admin.setKoiBalance(newBalance);
        setTransactions.add(transactions2);

        payment.setKoiTransactions(setTransactions);

        userRepository.save(admin);
        paymentRepository.save(payment);
    }
    
}
