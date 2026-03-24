package com.shoghl.shoghl_api.service;

import com.shoghl.shoghl_api.dto.request.CreateBookingRequest;
import com.shoghl.shoghl_api.dto.response.BookingResponse;
import com.shoghl.shoghl_api.model.Booking;
import com.shoghl.shoghl_api.model.enums.BookingStatus;
import com.shoghl.shoghl_api.model.enums.JobStatus;
import com.shoghl.shoghl_api.model.enums.UserRole;
import com.shoghl.shoghl_api.repository.BookingRepository;
import com.shoghl.shoghl_api.repository.JobRepository;
import com.shoghl.shoghl_api.repository.WorkerProfileRepository;
import com.shoghl.shoghl_api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final JobRepository jobRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @Transactional
    public BookingResponse sendOffer(CreateBookingRequest request) {
        var user = securityUtils.getCurrentUser();

        if (user.getRole() != UserRole.WORKER) {
            throw new IllegalArgumentException("Only workers can send offers");
        }

        var workerProfile = workerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Worker profile not found. Please create your profile first"));

        var job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new IllegalArgumentException("This job is no longer open");
        }

        if (bookingRepository.existsByJobIdAndWorkerId(job.getId(), workerProfile.getId())) {
            throw new IllegalArgumentException("You already sent an offer for this job");
        }

        Booking booking = Booking.builder()
                .job(job)
                .worker(workerProfile)
                .priceOffered(request.getPriceOffered())
                .message(request.getMessage())
                .status(BookingStatus.PENDING)
                .build();

        bookingRepository.save(booking);

        notificationService.send(
                job.getClient(),
                "New offer received",
                user.getFullName() + " sent an offer for your job: " + job.getTitle(),
                "NEW_OFFER"
        );

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse acceptOffer(UUID bookingId) {
        var user = securityUtils.getCurrentUser();

        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getJob().getClient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only accept offers on your own jobs");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("This offer is no longer pending");
        }

        booking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(booking);

        var job = booking.getJob();
        job.setStatus(JobStatus.ASSIGNED);
        jobRepository.save(job);

        notificationService.send(
                booking.getWorker().getUser(),
                "Offer accepted!",
                "Your offer for \"" + job.getTitle() + "\" was accepted",
                "OFFER_ACCEPTED"
        );

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse markDone(UUID bookingId) {
        var user = securityUtils.getCurrentUser();

        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getJob().getClient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Only the client can mark a job as done");
        }

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new IllegalArgumentException("Job must be accepted before marking as done");
        }

        booking.setStatus(BookingStatus.DONE);
        booking.getJob().setStatus(JobStatus.COMPLETED);
        jobRepository.save(booking.getJob());
        bookingRepository.save(booking);

        notificationService.send(
                booking.getWorker().getUser(),
                "Job completed!",
                "The client marked \"" + booking.getJob().getTitle() + "\" as completed. You can now receive a review.",
                "JOB_DONE"
        );

        return mapToResponse(booking);
    }
    @Transactional(readOnly = true)
    public List<BookingResponse> getJobOffers(UUID jobId) {
        return bookingRepository.findByJobIdOrderByCreatedAtDesc(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyOffers() {
        var user = securityUtils.getCurrentUser();
        var profile = workerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Worker profile not found"));
        return bookingRepository.findByWorkerIdOrderByCreatedAtDesc(profile.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .jobId(booking.getJob().getId())
                .jobTitle(booking.getJob().getTitle())
                .workerId(booking.getWorker().getId())
                .workerName(booking.getWorker().getUser().getFullName())
                .priceOffered(booking.getPriceOffered())
                .message(booking.getMessage())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}