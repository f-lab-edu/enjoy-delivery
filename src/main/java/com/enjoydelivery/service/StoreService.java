package com.enjoydelivery.service;

import com.enjoydelivery.dto.store.request.StoreRequestDTO;
import com.enjoydelivery.entity.Category;
import com.enjoydelivery.entity.Store;
import com.enjoydelivery.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final CategoryService categoryService;

  public List<Store> readAllByCategory(Long categoryId) {
    Category findCategory = categoryService.readOneById(categoryId);
    List<Store> stores = storeRepository.findStoreFetchJoinByCategory(findCategory);
    if (stores == null || stores.isEmpty()) {
      throw new RuntimeException();
    }
    return stores;
  }

  public Store readOneFetchJoinById(Long storeId) {
    Store findStore = storeRepository.findDistinctStoreFetchJoinById(storeId)
        .orElseThrow(RuntimeException::new);

    if (findStore.getCategory() == null) {
      throw new RuntimeException();
    }

    if (findStore.getMenus() == null) {
      throw new RuntimeException();
    }
    return findStore;
  }

  public Store readOneById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(RuntimeException::new);
  }

  public void create(StoreRequestDTO storeRequestDTO) {
    Store store = storeRequestDTO.toEntity();
    if (!storeRepository.existsByName(store.getName())) {
      storeRepository.save(store);
      return;
    }
    throw new RuntimeException();
  }

  @Transactional
  public void update(Long storeId, StoreRequestDTO storeRequestDTO) {
    Store findStore = readOneById(storeId);
    findStore.update(storeRequestDTO);
  }

  public Store readOneByOwnerId(Long ownerId) {
    return storeRepository.findStoreByOwner_Id(ownerId)
        .orElseThrow(RuntimeException::new);

  }
}