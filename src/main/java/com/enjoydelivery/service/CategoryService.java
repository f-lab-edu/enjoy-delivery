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

  private final CategoryRepository categoryRepository;

  public List<Category> readAll() {
    return categoryRepository.findAll();
  }

  public void create(CategoryRequestDTO categoryRequestDTO) {
    Category category = categoryRequestDTO.toEntity();

    if (!categoryRepository.existsByName(category.getName())) {
      categoryRepository.save(category);
    }

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
        .orElseThrow(RuntimeException::new);
  }

}
