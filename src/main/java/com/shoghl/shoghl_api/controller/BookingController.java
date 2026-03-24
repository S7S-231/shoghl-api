package com.shoghl.shoghl_api.controller;

import com.shoghl.shoghl_api.dto.request.CreateBookingRequest;
import com.shoghl.shoghl_api.dto.response.BookingResponse;
import com.shoghl.shoghl_api.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> sendOffer(
            @Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.sendOffer(request));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> acceptOffer(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.acceptOffer(id));
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<BookingResponse> markDone(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.markDone(id));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<BookingResponse>> getJobOffers(@PathVariable UUID jobId) {
        return ResponseEntity.ok(bookingService.getJobOffers(jobId));
    }

    @GetMapping("/my-offers")
    public ResponseEntity<List<BookingResponse>> getMyOffers() {
        return ResponseEntity.ok(bookingService.getMyOffers());
    }
}