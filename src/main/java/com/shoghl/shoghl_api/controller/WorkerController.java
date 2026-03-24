package com.shoghl.shoghl_api.controller;

import com.shoghl.shoghl_api.dto.request.WorkerProfileRequest;
import com.shoghl.shoghl_api.dto.response.WorkerProfileResponse;
import com.shoghl.shoghl_api.service.WorkerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerProfileService workerProfileService;

    @PutMapping("/profile")
    public ResponseEntity<WorkerProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody WorkerProfileRequest request) {
        return ResponseEntity.ok(workerProfileService.createOrUpdateProfile(request));
    }

    @GetMapping("/profile/me")
    public ResponseEntity<WorkerProfileResponse> getMyProfile() {
        return ResponseEntity.ok(workerProfileService.getMyProfile());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerProfileResponse> getWorkerById(@PathVariable UUID id) {
        return ResponseEntity.ok(workerProfileService.getProfileById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkerProfileResponse>> searchWorkers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer categoryId) {
        return ResponseEntity.ok(workerProfileService.searchWorkers(city, categoryId));
    }

    @PatchMapping("/availability")
    public ResponseEntity<Void> toggleAvailability() {
        workerProfileService.toggleAvailability();
        return ResponseEntity.ok().build();
    }
}