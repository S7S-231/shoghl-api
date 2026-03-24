package com.shoghl.shoghl_api.service;

import com.shoghl.shoghl_api.dto.request.CreateJobRequest;
import com.shoghl.shoghl_api.dto.response.JobResponse;
import com.shoghl.shoghl_api.model.Job;
import com.shoghl.shoghl_api.model.enums.JobStatus;
import com.shoghl.shoghl_api.model.enums.UserRole;
import com.shoghl.shoghl_api.repository.CategoryRepository;
import com.shoghl.shoghl_api.repository.JobRepository;
import com.shoghl.shoghl_api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public JobResponse createJob(CreateJobRequest request) {
        var user = securityUtils.getCurrentUser();

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("Only clients can post jobs");
        }

        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Job job = Job.builder()
                .client(user)
                .category(category)
                .title(request.getTitle())
                .description(request.getDescription())
                .city(request.getCity())
                .area(request.getArea())
                .budget(request.getBudget())
                .status(JobStatus.OPEN)
                .build();

        jobRepository.save(job);
        return mapToResponse(job);
    }
    @Transactional(readOnly = true)
    public List<JobResponse> getOpenJobs(String city, Integer categoryId) {
        if (city != null && categoryId != null) {
            return jobRepository
                    .findByCategoryIdAndStatusOrderByCreatedAtDesc(categoryId, JobStatus.OPEN)
                    .stream()
                    .filter(j -> j.getCity().equalsIgnoreCase(city))
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        if (city != null) {
            return jobRepository
                    .findByStatusAndCityOrderByCreatedAtDesc(JobStatus.OPEN, city)
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return jobRepository
                .findByStatusOrderByCreatedAtDesc(JobStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<JobResponse> getMyJobs() {
        var user = securityUtils.getCurrentUser();
        return jobRepository.findByClientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public JobResponse getJobById(UUID id) {
        return mapToResponse(jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found")));
    }

    @Transactional
    public void cancelJob(UUID id) {
        var user = securityUtils.getCurrentUser();
        var job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getClient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only cancel your own jobs");
        }
        if (job.getStatus() != JobStatus.OPEN) {
            throw new IllegalArgumentException("Only open jobs can be cancelled");
        }

        job.setStatus(JobStatus.CANCELLED);
        jobRepository.save(job);
    }

    private JobResponse mapToResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .clientId(job.getClient().getId())
                .clientName(job.getClient().getFullName())
                .categoryId(job.getCategory().getId())
                .categoryName(job.getCategory().getName())
                .title(job.getTitle())
                .description(job.getDescription())
                .city(job.getCity())
                .area(job.getArea())
                .budget(job.getBudget())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .build();
    }
}