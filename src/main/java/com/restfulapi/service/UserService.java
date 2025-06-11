package com.restfulapi.service;

import com.restfulapi.dto.CreateUserDTO;
import com.restfulapi.entity.User;
import com.restfulapi.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public List<User> fetchAllUser( Specification<User> spec,Pageable pageable){
        return userRepository.findAll(spec,pageable).getContent();
    }
    public User findByUsername(String email){
        return userRepository.findUserByEmail(email);
    }
    public CreateUserDTO createUser(User user){
        CreateUserDTO createUserDTO=new CreateUserDTO();
        createUserDTO.setId(user.getId());
        createUserDTO.setEmail(user.getEmail());
        createUserDTO.setName(user.getName());
        createUserDTO.setAge(user.getAge());
        createUserDTO.setGender(user.getGender());
        createUserDTO.setCreatedAt(user.getCreatedAt());
        createUserDTO.setAddress(user.getAddress());
        return createUserDTO;
    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
