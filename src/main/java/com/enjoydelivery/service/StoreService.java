package com.enjoydelivery.service;

import com.enjoydelivery.dto.request.StoreCommand;
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
    return storeRepository.findStoreFetchJoinByCategory(findCategory);
  }

  public Store readOneFetchJoinById(Long storeId) {
    return storeRepository.findDistinctStoreFetchJoinById(storeId)
        .orElseThrow(RuntimeException::new);
  }

  public Store readOneById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(RuntimeException::new);
  }

  public void create(StoreCommand storeCommand) {
    Store store = storeCommand.toEntity();
    if (!storeRepository.existsByName(store.getName())) {
      storeRepository.save(store);
    }
  }

  @Transactional
  public void update(Long storeId, StoreCommand storeCommand) {
    Store findStore = readOneById(storeId);
    findStore.update(storeCommand);
  }
}
