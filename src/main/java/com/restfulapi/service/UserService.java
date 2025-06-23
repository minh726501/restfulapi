package com.restfulapi.service;

import com.restfulapi.dto.CompanyDTO;
import com.restfulapi.dto.CreateUserDTO;
import com.restfulapi.dto.ResponseUpdateUserDTO;
import com.restfulapi.dto.ResponseUserDTO;
import com.restfulapi.entity.Company;
import com.restfulapi.entity.User;
import com.restfulapi.repository.CompanyRepository;
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
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, CompanyService companyService, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
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
        return responseUserDTO;
    }
    public ResponseUpdateUserDTO convertToResponseUpdateUserDTO(User user){
        ResponseUpdateUserDTO responseUpdateUserDTO=new ResponseUpdateUserDTO();
        CompanyDTO companyDTO=new CompanyDTO();
        responseUpdateUserDTO.setId(user.getId());
        responseUpdateUserDTO.setName(user.getName());
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
