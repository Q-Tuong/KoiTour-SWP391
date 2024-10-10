package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.*;
import com.koitourdemo.demo.model.OrderDetailRequest;
import com.koitourdemo.demo.model.OrderRequest;
import com.koitourdemo.demo.repository.KoiRepository;
import com.koitourdemo.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    OrderRepository orderRepository;

    public Orders createNewOrder(OrderRequest orderRequest){
        User customer = authenticationService.getCurrentUser();
        Orders order = new Orders();
        List<OrderDetail> orderDetails = new ArrayList<>();
        float total = 0;

        order.setCustomer(customer);
        order.setDate(new Date()); // current date

        for(OrderDetailRequest orderDetailRequest : orderRequest.getDetail()){
            Koi koi = koiRepository.findKoiById(orderDetailRequest.getKoiID());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(koi.getPrice());
            orderDetail.setOrder(order);
            orderDetail.setKoi(koi);
            orderDetails.add(orderDetail);

            total += koi.getPrice() * orderDetailRequest.getQuantity();
        }

        order.setOrderDetails(orderDetails);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Orders> getAllOrder(){
        User user = authenticationService.getCurrentUser();
        List<Orders> orders = orderRepository.findOrderssByCustomer(user);
        return orders;
    }
}
