package com.restfullapi.service;

import com.restfullapi.entity.User;
import com.restfullapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
    public Optional<User> fetchUserById(long id) {
        return userRepository.findById(id);
    }

    public List<User> fetchAllUser(){
        return userRepository.findAll();
    }
    public User findByUsername(String email){
        return userRepository.findUserByEmail(email);
    }
}
