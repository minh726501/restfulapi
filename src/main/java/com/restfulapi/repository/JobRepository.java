package com.restfulapi.repository;

import com.restfulapi.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job,Long> {
}
