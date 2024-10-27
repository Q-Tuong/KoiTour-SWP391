package com.koitourdemo.demo.service;

import com.koitourdemo.demo.enums.Role;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.AuthException;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    TourRepository tourRepository;

    @Autowired
    KoiTransactionRepository koiTransactionRepository;

    @Autowired
    TourTransactionRepository tourTransactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    public User updateUserRole(Role newRole, long id) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getId() == id) {
            throw new NotFoundException("Không thể sửa role của chính mình!");
        }

        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User deleteUser(long id){
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getId() == id) {
            throw new NotFoundException("Không thể xóa tài khoản của chính mình!");
        }

        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if (oldUser.isDeleted()) {
            throw new NotFoundException("Người dùng này đã bị xóa trước đó!");
        }
        oldUser.setDeleted(true);
        return userRepository.save(oldUser);
    }

    public Map<String, Object> getDashboardStats(){
        Map<String, Object> stats = new HashMap<>();
        // đếm số sản phẩm trong hệ thống
        long totalKois = koiRepository.count();
        stats.put("totalKois", totalKois);

        long totalTours = tourRepository.count();
        stats.put("totalTours", totalTours);

        // đếm số lượng user by role
        long managerCount = userRepository.countByRole(Role.MANAGER);
        stats.put("managerCount", managerCount);

        long consultingStaffCount = userRepository.countByRole(Role.CONSULTING_STAFF);
        stats.put("consultingStaffCount", consultingStaffCount);

        long saleStaffCount = userRepository.countByRole(Role.SALE_STAFF);
        stats.put("saleStaffCount", saleStaffCount);

        long customerCount = userRepository.countByRole(Role.CUSTOMER);
        stats.put("customerCount", customerCount);

        // Top 5 sản phẩm bán chạy nhất
        List<Object[]> topKois = koiRepository.findTop5BestSellingKoi();
        List<Map<String, Object>> topKoisList = new ArrayList<>();

        for (Object[] koiData : topKois) {
            Map<String, Object> koiInfo = new HashMap<>();
            koiInfo.put("koiName", koiData[0]);
            koiInfo.put("totalSold", koiData[1]);
            topKoisList.add(koiInfo);
        }
        stats.put("topKoi", topKoisList);

        List<Object[]> topTours = tourRepository.findTop5BestSellingTour();
        List<Map<String, Object>> topToursList = new ArrayList<>();

        for (Object[] tourData : topTours) {
            Map<String, Object> tourInfo = new HashMap<>();
            tourInfo.put("tourName", tourData[0]);
            tourInfo.put("totalSold", tourData[1]);
            topToursList.add(tourInfo);
        }
        stats.put("topTour", topToursList);

        return stats;
    }

    public Map<String, Object> getKoiMonthlyRevenue(){
        Map<String, Object> revenueData = new HashMap<>();
        User user = authenticationService.getCurrentUser();
        if(user == null){
            throw new AuthException("you have to login first to see");
        }
        List<Object[]> monthlyRevenue = koiTransactionRepository.calculateMonthlyRevenue(user.getId());
        List<Map<String, Object>> monthlyRevenueList = new ArrayList<>();
        for(Object[] result : monthlyRevenue){
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("year", result[0]);
            monthData.put("month", result[1]);
            monthData.put("totalRevenue", result[2]);
            monthlyRevenueList.add(monthData);
        }

        revenueData.put("monthlyRevenue", monthlyRevenueList);
        return revenueData;
    }

    public Map<String, Object> getTourMonthlyRevenue(){
        Map<String, Object> revenueData = new HashMap<>();
        User user = authenticationService.getCurrentUser();
        if(user == null){
            throw new AuthException("you have to login first to see");
        }
        List<Object[]> monthlyRevenue = tourTransactionRepository.calculateMonthlyRevenue(user.getId());
        List<Map<String, Object>> monthlyRevenueList = new ArrayList<>();
        for(Object[] result : monthlyRevenue){
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("year", result[0]);
            monthData.put("month", result[1]);
            monthData.put("totalRevenue", result[2]);
            monthlyRevenueList.add(monthData);
        }

        revenueData.put("monthlyRevenue", monthlyRevenueList);
        return revenueData;
    }

}
