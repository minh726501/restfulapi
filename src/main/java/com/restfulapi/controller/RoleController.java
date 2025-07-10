package com.restfulapi.controller;

import com.restfulapi.entity.Role;
import com.restfulapi.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role>createRole(@RequestBody Role role){
        return ResponseEntity.ok(roleService.createRole(role));
    }
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role>getRoleById(@PathVariable long id){
        Optional<Role>getRole=roleService.getRoleById(id);
        if (getRole.isEmpty()){
            throw new RuntimeException("Không tìm thấy Role với ID: " + id);
        }
        return ResponseEntity.ok(getRole.get());
    }
    @GetMapping("/roles")
    public ResponseEntity<List<Role>>getAllRole(){
        return ResponseEntity.ok(roleService.getAllRole());
    }
    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role){
        return ResponseEntity.ok(roleService.updateRole(role));
    }
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void>deleteRole(@PathVariable long id){
        Optional<Role>getRole=roleService.getRoleById(id);
        if (getRole.isEmpty()){
            throw new RuntimeException("Không tìm thấy Role với ID: " + id);
        }
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
