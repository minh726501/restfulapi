package com.restfulapi.service;

import com.restfulapi.dto.*;
import com.restfulapi.entity.Company;
import com.restfulapi.entity.Role;
import com.restfulapi.entity.User;
import com.restfulapi.repository.CompanyRepository;
import com.restfulapi.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;


    public UserService(UserRepository userRepository, CompanyService companyService, CompanyRepository companyRepository,RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService=roleService;
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

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public ResponseUserDTO convertToResponseUserDTO(User user) {
        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        CompanyDTO companyDTO=new CompanyDTO();
        RoleResponseDTO roleResponseDTO=new RoleResponseDTO();
        responseUserDTO.setId(user.getId());
        responseUserDTO.setEmail(user.getEmail());
        responseUserDTO.setName(user.getName());
        responseUserDTO.setAge(user.getAge());
        responseUserDTO.setGender(user.getGender());
        responseUserDTO.setCreatedAt(user.getCreatedAt());
        responseUserDTO.setAddress(user.getAddress());
        responseUserDTO.setUpdatedAt(user.getUpdatedAt());
        if (user.getCompany()!=null){
            Optional<Company> company=companyService.getCompanyById(user.getCompany().getId());
            if (company.isPresent()){
                companyDTO.setId(company.get().getId());
                companyDTO.setName(company.get().getName());
                responseUserDTO.setCompany(companyDTO);
            }
        }
        if (user.getRole()!=null){
            Optional<Role>getRole=roleService.getRoleById(user.getRole().getId());
            if (getRole.isPresent()){
                roleResponseDTO.setId(getRole.get().getId());
                roleResponseDTO.setName(getRole.get().getName());
                responseUserDTO.setRole(roleResponseDTO);
            }
        }
        return responseUserDTO;
    }
    public User updateUser(User user){
        Optional<User> existingUserOpt=fetchUserById(user.getId());
        if (existingUserOpt.isEmpty()){
            throw new RuntimeException("Không tìm thấy user với ID: " + user.getId());
        }
        User existingUser=existingUserOpt.get();
        existingUser.setName(user.getName());
        existingUser.setAge(user.getAge());
        existingUser.setAddress(user.getAddress());
        existingUser.setGender(user.getGender());
        existingUser.setUpdatedAt(Instant.now());
        // Nếu userRequest có company, cập nhật công ty
        if (user.getCompany()!=null){
            Company company=companyService.getCompanyById(user.getCompany().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy company với ID: " + user.getCompany().getId()));
            existingUser.setCompany(company);
        }
        if (user.getRole()!=null){
            Role role=roleService.getRoleById(user.getRole().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Role với ID: " + user.getRole().getId()));
            existingUser.setRole(role);
        }
        return userRepository.save(existingUser);
    }
    public ResponseUpdateUserDTO convertToResponseUpdateUserDTO(User user){
        ResponseUpdateUserDTO responseUpdateUserDTO=new ResponseUpdateUserDTO();
        CompanyDTO companyDTO=new CompanyDTO();
        RoleResponseDTO roleResponseDTO=new RoleResponseDTO();
        responseUpdateUserDTO.setId(user.getId());
        responseUpdateUserDTO.setName(user.getName());
        responseUpdateUserDTO.setAge(user.getAge());
        responseUpdateUserDTO.setAddress(user.getAddress());
        responseUpdateUserDTO.setGender(user.getGender());
        responseUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        if (user.getCompany()!=null){
            Optional<Company> company=companyService.getCompanyById(user.getCompany().getId());
            if (company.isPresent()){
                companyDTO.setId(user.getCompany().getId());
                companyDTO.setName(user.getCompany().getName());
                responseUpdateUserDTO.setCompany(companyDTO);
            }
        }
        if (user.getRole() != null) {
            Optional<Role>getRole=roleService.getRoleById(user.getRole().getId());
            if (getRole.isPresent()){
                roleResponseDTO.setId(getRole.get().getId());
                roleResponseDTO.setName(getRole.get().getName());
                responseUpdateUserDTO.setRole(roleResponseDTO);
            }
        }
        return  responseUpdateUserDTO;
    }
    public void updateToken(String token,String email){
        User getuser=userRepository.findUserByEmail(email);
        if (getuser!=null){
            getuser.setRefreshToken(token);
            userRepository.save(getuser);
        }
    }

}
