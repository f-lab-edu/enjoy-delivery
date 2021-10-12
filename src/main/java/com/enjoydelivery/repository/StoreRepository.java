package com.enjoydelivery.repository;

import com.enjoydelivery.entity.Category;
import com.enjoydelivery.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  boolean existsByName(String name);

  @EntityGraph(attributePaths ={"category", "owner"})
  List<Store> findStoreFetchJoinByCategory(Category category);

  @EntityGraph(attributePaths = {"category", "owner", "menus"})
  Optional<Store> findDistinctStoreFetchJoinById(Long id);
}