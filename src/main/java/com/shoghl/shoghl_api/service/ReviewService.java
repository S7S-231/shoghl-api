package com.shoghl.shoghl_api.service;

import com.shoghl.shoghl_api.dto.request.CreateReviewRequest;
import com.shoghl.shoghl_api.model.Review;
import com.shoghl.shoghl_api.model.enums.BookingStatus;
import com.shoghl.shoghl_api.repository.BookingRepository;
import com.shoghl.shoghl_api.repository.ReviewRepository;
import com.shoghl.shoghl_api.repository.WorkerProfileRepository;
import com.shoghl.shoghl_api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public void createReview(CreateReviewRequest request) {
        var user = securityUtils.getCurrentUser();

        var booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getJob().getClient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Only the client can review this booking");
        }

        if (booking.getStatus() != BookingStatus.DONE) {
            throw new IllegalArgumentException("Can only review completed bookings");
        }

        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new IllegalArgumentException("You already reviewed this booking");
        }

        var workerUser = booking.getWorker().getUser();

        Review review = Review.builder()
                .booking(booking)
                .reviewer(user)
                .reviewee(workerUser)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);

        // recalculate worker avg rating
        var workerProfile = booking.getWorker();
        Double avg = reviewRepository.calculateAvgRating(workerUser.getId());
        long count = reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(workerUser.getId()).size();

        workerProfile.setAvgRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
        workerProfile.setTotalReviews((int) count);
        workerProfileRepository.save(workerProfile);
    }
}