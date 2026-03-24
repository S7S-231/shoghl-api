package com.shoghl.shoghl_api.repository;

import com.shoghl.shoghl_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}