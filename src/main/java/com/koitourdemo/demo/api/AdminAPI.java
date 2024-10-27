package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.model.request.RoleUpdateRequest;
import com.koitourdemo.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AdminAPI {

    @Autowired
    AdminService adminService;

    @PutMapping("/{userId}/update-role")
    public ResponseEntity updateUserRole(@PathVariable long userId, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        User updated = adminService.updateUserRole(roleUpdateRequest.getRole(), userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllUser(){
        List<User> users = adminService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity deleteUser(@PathVariable long userId){
        User deleted = adminService.deleteUser(userId);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/stats")
    public ResponseEntity getDashBoardStats(){
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly-revenue/koi")
    public ResponseEntity getKoiMonthlyRevenue(){
        Map<String, Object> monthlyRevenue = adminService.getKoiMonthlyRevenue();
        return ResponseEntity.ok(monthlyRevenue);
    }

    @GetMapping("/monthly-revenue/tour")
    public ResponseEntity getTourMonthlyRevenue(){
        Map<String, Object> monthlyRevenue = adminService.getTourMonthlyRevenue();
        return ResponseEntity.ok(monthlyRevenue);
    }

}
