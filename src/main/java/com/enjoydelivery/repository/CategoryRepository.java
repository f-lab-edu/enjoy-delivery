package com.enjoydelivery.repository;

import com.enjoydelivery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  boolean existsByName(String name);
}
