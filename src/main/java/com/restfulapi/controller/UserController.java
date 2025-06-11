package com.restfulapi.controller;

import com.restfulapi.annotation.ApiMessage;
import com.restfulapi.dto.CreateUserDTO;
import com.restfulapi.entity.User;
import com.restfulapi.exception.ResourceNotFoundException;
import com.restfulapi.service.UserService;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create User")
    public ResponseEntity<CreateUserDTO> createUser(@Valid @RequestBody User user) {
        boolean existsEmail=userService.existsByEmail(user.getEmail());
        if (existsEmail){
            throw new RuntimeException ("Email existed");
        }
        user.setCreatedAt(Instant.now());
        String hashPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        User newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
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
    public User fetchUserById(@PathVariable long id) {
        return userService.fetchUserById(id).orElseThrow(() -> new ResourceNotFoundException(("Không tìm thấy User với ID: " + id)));

    }

    @GetMapping("/users")
    @ApiMessage("Fetch All User")
    public ResponseEntity<List<User>> fetchAllUser(@Filter Specification<User> spec,@RequestParam(value = "page",required = false) int page, @RequestParam(value = "size",required = false)int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        return ResponseEntity.ok(userService.fetchAllUser(spec,pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update User")
    public ResponseEntity<?> updateUSer(@RequestBody User user) {
        Optional<User> getUser = userService.fetchUserById(user.getId());
        if (getUser.isPresent()) {
            User updateUser = getUser.get();
            updateUser.setName(user.getName());
            updateUser.setEmail(user.getEmail());
            String hasPass=passwordEncoder.encode(user.getPassword());
            updateUser.setPassword(hasPass);
            userService.saveUser(updateUser);
            return ResponseEntity.ok().body(updateUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy user với ID: " + user.getId());
    }
}


