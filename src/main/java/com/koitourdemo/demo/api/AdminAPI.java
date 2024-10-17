package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.request.RoleUpdateRequest;
import com.koitourdemo.demo.model.response.ApiResponse;
import com.koitourdemo.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AdminAPI {

    @Autowired
    AdminService adminService;

    @PutMapping("/{id}/update-role")
    public ResponseEntity<ApiResponse> updateUserRole(@PathVariable long id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        try {
            User updated = adminService.updateUserRole(roleUpdateRequest.getRole(), id);
            return ResponseEntity.ok(new ApiResponse("Update role successfully!", updated));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to update role!", null));
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllUser(){
        try {
            List<User> users = adminService.getAllUser();
            return ResponseEntity.ok(new ApiResponse("Get all user successfully!", users));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to get all users!", null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id){
        try {
            User deleted = adminService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("Delete user successfully!", deleted));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed to delete user!", null));
        }
    }

}
