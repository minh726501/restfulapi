package com.restfulapi.controller;

import com.restfulapi.entity.User;
import com.restfulapi.exception.ResourceNotFoundException;
import com.restfulapi.service.UserService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String hashPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        User newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("delete User By id" + id);
    }

    @GetMapping("/users/{id}")
    public User fetchUserById(@PathVariable long id) {
        return userService.fetchUserById(id).orElseThrow(() -> new ResourceNotFoundException(("Không tìm thấy User với ID: " + id)));

    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUser(@RequestParam(value = "page",required = false) int page,@RequestParam(value = "size",required = false)int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        return ResponseEntity.ok(userService.fetchAllUser(pageable));
    }

    @PutMapping("/users")
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


