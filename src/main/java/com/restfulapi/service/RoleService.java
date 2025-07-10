package com.restfulapi.service;

import com.restfulapi.entity.Permission;
import com.restfulapi.entity.Role;
import com.restfulapi.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;


    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role với tên '" + role.getName() + "' đã tồn tại");
        }
        //check existingPermission
        if (role.getPermissions() != null) {
            List<Permission> validPer = new ArrayList<>();
            for (Permission per : role.getPermissions()) {
                Long permissionId = per.getId();
                Optional<Permission> getPer = permissionService.getPerById(per.getId());
                if (getPer.isEmpty()) {
                    throw new RuntimeException("Permission với ID " + permissionId + " không tồn tại");
                }
                validPer.add(getPer.get());
            }
            role.setPermissions(validPer);
        }
        return roleRepository.save(role);
    }

    public Optional<Role> getRoleById(long id) {
        return roleRepository.findById(id);
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public Role updateRole(Role role) {
        Optional<Role> getRole = getRoleById(role.getId());
        if (getRole.isEmpty()) {
            throw new RuntimeException("Không tìm thấy Role với ID: " + role.getId());
        }
        Role existingRole = getRole.get();
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setActive(role.isActive());
        existingRole.setPermissions(role.getPermissions());
        return roleRepository.save(existingRole);
    }
    public void deleteRole(long id){
        roleRepository.deleteById(id);
    }
    public Role findByName(String name){
        return roleRepository.findByName(name);
    }
}
