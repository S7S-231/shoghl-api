package com.shoghl.shoghl_api.repository;

import com.shoghl.shoghl_api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByRevieweeIdOrderByCreatedAtDesc(UUID revieweeId);
    boolean existsByBookingId(UUID bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :userId")
    Double calculateAvgRating(@Param("userId") UUID userId);
}