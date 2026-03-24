package com.shoghl.shoghl_api.service;

import com.shoghl.shoghl_api.dto.request.WorkerProfileRequest;
import com.shoghl.shoghl_api.dto.response.WorkerProfileResponse;
import com.shoghl.shoghl_api.model.Category;
import com.shoghl.shoghl_api.model.WorkerProfile;
import com.shoghl.shoghl_api.model.enums.UserRole;
import com.shoghl.shoghl_api.repository.CategoryRepository;
import com.shoghl.shoghl_api.repository.WorkerProfileRepository;
import com.shoghl.shoghl_api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerProfileService {

    private final WorkerProfileRepository workerProfileRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public WorkerProfileResponse createOrUpdateProfile(WorkerProfileRequest request) {
        var user = securityUtils.getCurrentUser();

        if (user.getRole() != UserRole.WORKER) {
            throw new IllegalArgumentException("Only workers can create a profile");
        }

        Set<Category> categories = new HashSet<>(
                categoryRepository.findAllById(request.getCategoryIds())
        );

        WorkerProfile profile = workerProfileRepository.findByUserId(user.getId())
                .orElse(WorkerProfile.builder().user(user).build());

        profile.setBio(request.getBio());
        profile.setYearsExperience(request.getYearsExperience());
        profile.setCity(request.getCity());
        profile.setArea(request.getArea());
        profile.setCategories(categories);

        workerProfileRepository.save(profile);
        return mapToResponse(profile);
    }
    @Transactional(readOnly = true)
    public WorkerProfileResponse getMyProfile() {
        var user = securityUtils.getCurrentUser();
        var profile = workerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        return mapToResponse(profile);
    }
    @Transactional(readOnly = true)
    public WorkerProfileResponse getProfileById(UUID id) {
        var profile = workerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        return mapToResponse(profile);
    }
    @Transactional(readOnly = true)
    public List<WorkerProfileResponse> searchWorkers(String city, Integer categoryId) {
        return workerProfileRepository.searchWorkers(city, categoryId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleAvailability() {
        var user = securityUtils.getCurrentUser();
        var profile = workerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        profile.setAvailable(!profile.isAvailable());
        workerProfileRepository.save(profile);
    }

    private WorkerProfileResponse mapToResponse(WorkerProfile profile) {
        return WorkerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .phone(profile.getUser().getPhone())
                .bio(profile.getBio())
                .yearsExperience(profile.getYearsExperience())
                .city(profile.getCity())
                .area(profile.getArea())
                .isAvailable(profile.isAvailable())
                .avgRating(profile.getAvgRating())
                .totalReviews(profile.getTotalReviews())
                .categories(profile.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}