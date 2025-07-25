package com.restfulapi.controller;

import com.restfulapi.annotation.ApiMessage;
import com.restfulapi.dto.ResponseUpdateUserDTO;
import com.restfulapi.dto.ResponseUserDTO;
import com.restfulapi.entity.User;
import com.restfulapi.service.RoleService;
import com.restfulapi.service.UserService;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder,RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService=roleService;
    }

    @PostMapping("/users")
    @ApiMessage("Create User")
    public ResponseEntity<ResponseUserDTO> createUser(@Valid @RequestBody User user) {
        boolean existsEmail=userService.existsByEmail(user.getEmail());
        if (existsEmail){
            throw new RuntimeException ("Email existed");
        }
        if (user.getRole()!=null && user.getRole().getId()>0){
            roleService.getRoleById(user.getRole().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Role với ID: " + user.getRole().getId()));
        }
        user.setCreatedAt(Instant.now());
        String hashPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        User newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResponseUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        Optional<User> getUser=userService.fetchUserById(id);
        if (getUser.isEmpty()){
            throw new RuntimeException("Không tìm thấy user với ID: " + id);

        }
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch User")
    public ResponseEntity<ResponseUserDTO> fetchUserById(@PathVariable long id) {
        Optional<User> getUser= userService.fetchUserById(id);
        if (getUser.isEmpty()){
            throw new RuntimeException("Không tìm thấy user với ID: " + id);
        }
        return ResponseEntity.ok(userService.convertToResponseUserDTO(getUser.get()));

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    @ApiMessage("Fetch All User")
    public ResponseEntity<List<ResponseUserDTO>> fetchAllUser(@Filter Specification<User> spec,@RequestParam(value = "page",required = false) int page, @RequestParam(value = "size",required = false)int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        return ResponseEntity.ok(userService.fetchAllUser(spec,pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update User")
    public ResponseEntity<ResponseUpdateUserDTO> updateUSer(@RequestBody User user) {
        Optional<User> getUser = userService.fetchUserById(user.getId());
        if (getUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy user với ID: " + user.getId());
        }
        User updateUser=userService.updateUser(user);
        return ResponseEntity.ok(userService.convertToResponseUpdateUserDTO(updateUser));
    }
}


