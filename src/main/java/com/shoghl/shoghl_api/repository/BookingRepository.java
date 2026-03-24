package com.shoghl.shoghl_api.repository;

import com.shoghl.shoghl_api.model.Booking;
import com.shoghl.shoghl_api.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByJobIdOrderByCreatedAtDesc(UUID jobId);
    List<Booking> findByWorkerIdOrderByCreatedAtDesc(UUID workerId);
    boolean existsByJobIdAndWorkerId(UUID jobId, UUID workerId);
    Optional<Booking> findByIdAndStatus(UUID id, BookingStatus status);
    List<Booking> findByJobIdAndStatus(UUID jobId, BookingStatus status);
}