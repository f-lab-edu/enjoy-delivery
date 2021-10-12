package com.enjoydelivery.service;

import com.enjoydelivery.dto.request.CategoryCommand;
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

  public void create(CategoryCommand categoryCommand) {
    Category category = categoryCommand.toEntity();

    if (!categoryRepository.existsByName(category.getName())) {
      categoryRepository.save(category);
    }

  }

  @Transactional
  public void update(Long categoryId, CategoryCommand updateCategoryCommand) {
    Category findCategory = readOneById(categoryId);
    findCategory.update(updateCategoryCommand);
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
