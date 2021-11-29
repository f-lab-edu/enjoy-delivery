package com.enjoydelivery.repository;

import com.enjoydelivery.entity.Category;
import com.enjoydelivery.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  boolean existsByName(String name);

  @EntityGraph(attributePaths ={"category"})
  List<Store> findStoreFetchJoinByCategory(Category category);

  @EntityGraph(attributePaths = {"category", "menus"})
  Optional<Store> findDistinctStoreFetchJoinById(Long id);

  Optional<Store> findStoreByOwner_Id(Long ownerId);
}