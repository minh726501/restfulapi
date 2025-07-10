package com.restfulapi.repository;

import com.restfulapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(String name);
    Role findByName(String name);
}
