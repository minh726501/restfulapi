package com.restfulapi.repository;

import com.restfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);
    boolean existsByEmail(String email);

}
