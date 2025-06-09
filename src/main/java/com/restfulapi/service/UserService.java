package com.restfulapi.service;

import com.restfulapi.entity.User;
import com.restfulapi.repository.UserRepository;
import org.springframework.data.domain.Pageable;
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

    public List<User> fetchAllUser(Pageable pageable){
        return userRepository.findAll(pageable).getContent();
    }
    public User findByUsername(String email){
        return userRepository.findUserByEmail(email);
    }
}
