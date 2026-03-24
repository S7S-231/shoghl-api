package com.shoghl.shoghl_api.repository;

import com.shoghl.shoghl_api.model.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, UUID> {
    Optional<WorkerProfile> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);

    @Query("""
        SELECT w FROM WorkerProfile w
        JOIN w.categories c
        WHERE (:city IS NULL OR w.city = :city)
        AND (:categoryId IS NULL OR c.id = :categoryId)
        AND w.isAvailable = true
        ORDER BY w.avgRating DESC
    """)
    List<WorkerProfile> searchWorkers(
            @Param("city") String city,
            @Param("categoryId") Integer categoryId
    );
}