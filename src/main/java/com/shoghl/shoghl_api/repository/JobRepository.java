package com.shoghl.shoghl_api.repository;

import com.shoghl.shoghl_api.model.Job;
import com.shoghl.shoghl_api.model.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByClientIdOrderByCreatedAtDesc(UUID clientId);
    List<Job> findByStatusAndCityOrderByCreatedAtDesc(JobStatus status, String city);
    List<Job> findByStatusOrderByCreatedAtDesc(JobStatus status);
    List<Job> findByCategoryIdAndStatusOrderByCreatedAtDesc(Integer categoryId, JobStatus status);
}