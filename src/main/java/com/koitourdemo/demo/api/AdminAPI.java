package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.model.RoleUpdateRequest;
import com.koitourdemo.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AdminAPI {

    @Autowired
    AdminService adminService;

    @PutMapping("{id}")
    public ResponseEntity updateUserRole(@PathVariable long id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        User updatedUser = adminService.updateUserRole(roleUpdateRequest.getRole(), id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity getAllUser(){
        List<User> users = adminService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable long id){
        User deleteUser = adminService.deleteUser(id);
        return ResponseEntity.ok(deleteUser);
    }
}
