package com.restfulapi.repository;

import com.restfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);

}
