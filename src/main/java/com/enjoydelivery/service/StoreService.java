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

  public static final String EXCEPTION_NOT_FOUND_STORE = "조회되는 가게 데이터가 없습니다.";
  public static final String EXCEPTION_DUPLICATE_NAME = "이미 등록된 가게 이름입니다.";
  public static final String EXCEPTION_NOT_FOUND_MENUS = "조회되는 메뉴 데이터가 없습니다.";
  public static final String EXCEPTION_NOT_FOUND_CATEGORY = "조회되는 카테고리 데이터가 없습니다.";
  private final StoreRepository storeRepository;
  private final CategoryService categoryService;



  public List<Store> readAllByCategory(Long categoryId) {
    Category findCategory = categoryService.readOneById(categoryId);
    List<Store> stores = storeRepository.findStoreFetchJoinByCategory(findCategory);
    if (stores == null || stores.isEmpty()) {
      throw new RuntimeException(EXCEPTION_NOT_FOUND_STORE);
    }
    return stores;
  }

  public Store readOneFetchJoinById(Long storeId) {
    Store findStore = storeRepository.findDistinctStoreFetchJoinById(storeId)
        .orElseThrow(() -> new RuntimeException(EXCEPTION_NOT_FOUND_STORE));

    if (findStore.getCategory() == null) {
      throw new RuntimeException(EXCEPTION_NOT_FOUND_CATEGORY);
    }

    if (findStore.getMenus() == null) {
      throw new RuntimeException(EXCEPTION_NOT_FOUND_MENUS);
    }
    return findStore;
  }

  public Store readOneById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new RuntimeException(EXCEPTION_NOT_FOUND_STORE));
  }

  public void create(StoreRequestDTO storeRequestDTO) {
    Store store = storeRequestDTO.toEntity();
    if (!storeRepository.existsByName(store.getName())) {
      storeRepository.save(store);
      return;
    }
    throw new RuntimeException(EXCEPTION_DUPLICATE_NAME);
  }

  @Transactional
  public void update(Long storeId, StoreRequestDTO storeRequestDTO) {
    Store findStore = readOneById(storeId);
    findStore.update(storeRequestDTO);
  }

  public Store readOneByOwnerId(Long ownerId) {
    return storeRepository.findStoreByOwner_Id(ownerId)
        .orElseThrow(() -> new RuntimeException(EXCEPTION_NOT_FOUND_STORE));

  }
}