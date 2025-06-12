package com.restfulapi.service;

import com.restfulapi.dto.CreateUserDTO;
import com.restfulapi.dto.ResponseUpdateUserDTO;
import com.restfulapi.dto.ResponseUserDTO;
import com.restfulapi.entity.User;
import com.restfulapi.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ResponseUserDTO> fetchAllUser( Specification<User> spec,Pageable pageable){
         List<User> getList=userRepository.findAll(spec,pageable).getContent();
         List<ResponseUserDTO>responseUserDTOList=new ArrayList<>();
         for (User user:getList){
             ResponseUserDTO dto=convertToResponseUserDTO(user);
             responseUserDTOList.add(dto);
         }
         return responseUserDTOList;
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
    public ResponseUserDTO convertToResponseUserDTO(User user){
        ResponseUserDTO responseUserDTO=new ResponseUserDTO();
        responseUserDTO.setId(user.getId());
        responseUserDTO.setEmail(user.getEmail());
        responseUserDTO.setName(user.getName());
        responseUserDTO.setAge(user.getAge());
        responseUserDTO.setGender(user.getGender());
        responseUserDTO.setCreatedAt(user.getCreatedAt());
        responseUserDTO.setAddress(user.getAddress());
        responseUserDTO.setUpdatedAt(user.getUpdatedAt());
        return responseUserDTO;
    }
    public ResponseUpdateUserDTO convertToResponseUpdateUserDTO(User user){
        ResponseUpdateUserDTO responseUpdateUserDTO=new ResponseUpdateUserDTO();
        responseUpdateUserDTO.setId(user.getId());
        responseUpdateUserDTO.setName(user.getName());
        responseUpdateUserDTO.setAddress(user.getAddress());
        responseUpdateUserDTO.setGender(user.getGender());
        responseUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        return  responseUpdateUserDTO;
    }
}
