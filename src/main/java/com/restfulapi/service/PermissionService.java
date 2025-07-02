package com.restfulapi.service;

import com.restfulapi.dto.PermissionResponseDTO;
import com.restfulapi.entity.Permission;
import com.restfulapi.entity.Role;
import com.restfulapi.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public PermissionResponseDTO convertToPermissionDTO(Permission permission){
        PermissionResponseDTO permissionResponseDTO=new PermissionResponseDTO();
        permissionResponseDTO.setName(permission.getName());
        permissionResponseDTO.setApiPath(permission.getApiPath());
        permissionResponseDTO.setMethod(permission.getMethod());
        permissionResponseDTO.setModule(permission.getModule());
        return permissionResponseDTO;
    }
    public Permission createPermission(Permission permission){
        return permissionRepository.save(permission);
    }
    public List<PermissionResponseDTO>getAllPermissions(){
        List<PermissionResponseDTO> permissionResponseDTOS=new ArrayList<>();
        List<Permission> permissions=permissionRepository.findAll();
        for (Permission permission:permissions){
            PermissionResponseDTO dto=convertToPermissionDTO(permission);
            permissionResponseDTOS.add(dto);
        }
        return permissionResponseDTOS;
    }
    public Optional<Permission> getPerById(long id){
        return permissionRepository.findById(id);
    }
    public PermissionResponseDTO updatePer(Permission permission){
        Optional<Permission>getPer=getPerById(permission.getId());
        if (getPer.isEmpty()){
            throw new RuntimeException("Permission ID " + permission.getId() + " không tồn tại");
        }
        Permission updatePer=getPer.get();
        updatePer.setId(permission.getId());
        updatePer.setName(permission.getName());
        updatePer.setApiPath(permission.getApiPath());
        updatePer.setMethod(permission.getMethod());
        updatePer.setMethod(permission.getMethod());
        Permission savePer=permissionRepository.save(updatePer);
        return convertToPermissionDTO(savePer);
    }
    public void deletePer(long id){
        Optional<Permission>getPer=getPerById(id);
        if (getPer.isEmpty()){
            throw new RuntimeException("Permission ID không tồn tại");
        }
        Permission existingPer= getPer.get();
        for (Role role: existingPer.getRoles()){
            role.getPermissions().remove(existingPer);
        }
        permissionRepository.delete(existingPer);
    }
}
