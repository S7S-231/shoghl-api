package com.shoghl.shoghl_api.controller;

import com.shoghl.shoghl_api.dto.request.CreateReviewRequest;
import com.shoghl.shoghl_api.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(@Valid @RequestBody CreateReviewRequest request) {
        reviewService.createReview(request);
        return ResponseEntity.ok().build();
    }
}