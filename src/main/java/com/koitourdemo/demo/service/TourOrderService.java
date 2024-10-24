package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.*;
import com.koitourdemo.demo.enums.PaymentEnums;
import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.enums.TransactionsEnum;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.KoiOrderRequest;
import com.koitourdemo.demo.model.request.TourOrderRequest;
import com.koitourdemo.demo.model.request.TourOrderDetailRequest;
import com.koitourdemo.demo.repository.TourOrderRepository;
import com.koitourdemo.demo.repository.TourRepository;
import com.koitourdemo.demo.repository.UserRepository;
import com.koitourdemo.demo.repository.PaymentRepository;
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
public class TourOrderService {
    @Autowired
    AuthenticationService authenticationService;

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
        List<TourOrderDetail> orderDetails = new ArrayList<>();
        order.setCustomer(customer);
        order.setCreateAt(new Date());
        float orderTotal = 0;

        for (TourOrderDetailRequest detail : orderRequest.getDetails()) {
            Tour tour = tourRepository.findById(detail.getTourId())
                    .orElseThrow(() -> new NotFoundException("Tour not found with id: " + detail.getTourId()));

            if (tour.getPrice() != detail.getUnitPrice()) {
                throw new NotFoundException("Price mismatch for tour: " + tour.getId());
            }

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
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public List<TourOrder> getAllOrder(){
        User user = authenticationService.getCurrentUser();
        List<TourOrder> orders = orderRepository.findTourOderByCustomer(user);
        return orders;
    }

    public String createUrl(TourOrderRequest orderRequest) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        // code của mình
        // tạo order
        TourOrder orders = createTourOrder(orderRequest);

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

    public void createNewTourTransactions(UUID uuid) {
        TourOrder orders = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        Payment payment = new Payment();
        payment.setTourOrder(orders);
        payment.setCreateAt(new Date());
        payment.setPaymentMethod(PaymentEnums.BANKING);

        Set<Transactions> setTransactions = new HashSet<>();

        // VNPay to customer
        Transactions transactions1 = new Transactions();
        User customer = authenticationService.getCurrentUser();
        transactions1.setFrom(null);
        transactions1.setTo(customer);
        transactions1.setPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("NAP TIEN VNPAY TO CUSTOMER");
        setTransactions.add(transactions1);

        // CUSTOMER TO ADMIN
        Transactions transactions2 = new Transactions();
        User admin = userRepository.findUserByRole(Role.ADMIN);
        transactions2.setFrom(customer);
        transactions2.setTo(admin);
        transactions2.setPayment(payment);
        transactions2.setStatus(TransactionsEnum.SUCCESS);
        transactions2.setDescription("CUSTOMER TO ADMIN");
        float newBalance = admin.getTourBalance() + orders.getTotal();
        admin.setTourBalance(newBalance);
        setTransactions.add(transactions2);

        payment.setTransactions(setTransactions);
        userRepository.save(admin);
        paymentRepository.save(payment);
    }
}
