package com.enjoydelivery.service;

import com.enjoydelivery.dto.category.request.CategoryRequestDTO;
import com.enjoydelivery.entity.Category;
import com.enjoydelivery.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  public static final String EXCEPTION_NOT_FOUND_CATEGORY
      = "조회되는 카테고리 데이터가 없습니다.";
  public static final String EXCEPTION_DUPLICATE_NAME =
      "중복된 이름입니다.";

  private final CategoryRepository categoryRepository;

  public List<Category> readAll() {

    List<Category> categories =
        categoryRepository.findAll();

    if (categories == null || categories.isEmpty()) {
      throw new RuntimeException(EXCEPTION_NOT_FOUND_CATEGORY);
    }

    return categories;
  }

  public void create(CategoryRequestDTO categoryRequestDTO) {
    Category category = categoryRequestDTO.toEntity();

    if (categoryRepository.existsByName(category.getName())) {
      throw new RuntimeException(EXCEPTION_DUPLICATE_NAME);
    }
    categoryRepository.save(category);

  }

  @Transactional
  public void update(Long categoryId, CategoryRequestDTO updateCategoryRequestDTO) {
    Category findCategory = readOneById(categoryId);
    findCategory.update(updateCategoryRequestDTO);
  }

  public void delete(Long categoryId) {
    Category findCategory = readOneById(categoryId);
    categoryRepository.delete(findCategory);
  }

  public Category readOneById(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException(EXCEPTION_NOT_FOUND_CATEGORY));
  }

}
