package com.restfulapi.controller;

import com.restfulapi.dto.PermissionResponseDTO;
import com.restfulapi.entity.Permission;
import com.restfulapi.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionResponseDTO> createPermission(@Valid @RequestBody Permission permission) {
        Permission createPermission = permissionService.createPermission(permission);
        return ResponseEntity.ok(permissionService.convertToPermissionDTO(createPermission));
    }

    @GetMapping("/permissions")
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<PermissionResponseDTO> getPerById(@PathVariable long id) {
        Optional<Permission> getPer = permissionService.getPerById(id);
        if (getPer.isEmpty()) {
            throw new RuntimeException("Permission ID không tồn tại");
        }
        return ResponseEntity.ok(permissionService.convertToPermissionDTO(getPer.get()));
    }
    @PutMapping("/permissions")
    public ResponseEntity<PermissionResponseDTO>updatePer(@RequestBody Permission permission){
        return ResponseEntity.ok(permissionService.updatePer(permission));
    }
    @DeleteMapping("/permission/{id}")
    public ResponseEntity<Void>deletePer(@PathVariable long id){
        Optional<Permission>getPer=permissionService.getPerById(id);
        permissionService.deletePer(id);
        return ResponseEntity.noContent().build();
    }

}



