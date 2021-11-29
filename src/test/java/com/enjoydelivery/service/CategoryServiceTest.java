package com.enjoydelivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.enjoydelivery.dto.category.request.CategoryRequestDTO;
import com.enjoydelivery.entity.Category;
import com.enjoydelivery.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @InjectMocks
  CategoryService categoryService;

  @Mock
  CategoryRepository categoryRepository;

  public Category makeCategory() {
    return makeCategory(makeCategoryRequestDTO());
  }

  public Category makeCategory(CategoryRequestDTO categoryRequestDTO) {
    Category category = categoryRequestDTO.toEntity();
    category.setId(1L);
    return category;
  }

  public CategoryRequestDTO makeCategoryRequestDTO() {
    return new CategoryRequestDTO("중식");
  }

  public CategoryRequestDTO makeUpdateCategoryRequestDTO() {
    return new CategoryRequestDTO("양식");
  }

  @Test
  @DisplayName("카테고리 목록 조회 성공")
  void readAllSuccess() {

    // Arrange
    List<Category> categories = new ArrayList<>();
    Category category = makeCategory();
    categories.add(category);

    doReturn(categories)
        .when(categoryRepository)
        .findAll();

    // Act
    List<Category> actual = categoryService.readAll();

    // Assert
    assertThat(actual.size()).isEqualTo(categories.size());
    assertThat(actual).isEqualTo(categories);
    assertThat(actual.get(0)).isEqualTo(categories.get(0));

  }

  @Test
  @DisplayName("카테고리 목록 조회 실패 : 조회되는 카테고리 목록이 없을 경우")
  void readAllFail() {
    // Arrange
    List<Category> categories = new ArrayList<>();
    Category category = makeCategory();

    doReturn(new ArrayList<>())
        .when(categoryRepository)
        .findAll();

    // Act, assert
    assertThatThrownBy(() -> {
     categoryService.readAll();
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CategoryService.EXCEPTION_NOT_FOUND_CATEGORY);
  }

  @Test
  @DisplayName("카테고리 등록 성공")
  void createSuccess() {

    // Arrange
    CategoryRequestDTO categoryRequestDTO = makeCategoryRequestDTO();
    String categoryName = categoryRequestDTO.getName();

    doReturn(false)
        .when(categoryRepository)
        .existsByName(categoryName);

    // Act
    categoryService.create(categoryRequestDTO);

    // Assert
    verify(categoryRepository, times(1))
        .save(argThat(c ->
            c.getName().equals(categoryRequestDTO.getName())));

  }

  @Test
  @DisplayName("카테고리 등록 실패 : 이미 등록된 카테고리명이 존재함")
  void createFail() {
    // Arrange
    CategoryRequestDTO categoryRequestDTO = makeCategoryRequestDTO();
    String categoryName = categoryRequestDTO.getName();

    doReturn(true)
        .when(categoryRepository)
        .existsByName(categoryName);

    // Act
    assertThatThrownBy(() -> {
      categoryService.create(categoryRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CategoryService.EXCEPTION_DUPLICATE_NAME);

    //Assert
    verify(categoryRepository, times(0))
        .save(any(Category.class));

  }

  @Test
  @DisplayName("카테고리 수정 성공")
  void updateSuccess() {

    // Arrange
    CategoryRequestDTO updateCategoryRequestDTO
        = makeUpdateCategoryRequestDTO();

    Category updateCategory = makeCategory();
    Long categoryId = updateCategory.getId();

    Category originCategory = makeCategory();

    doReturn(Optional.of(updateCategory))
        .when(categoryRepository)
        .findById(categoryId);

    // Act
    categoryService.update(categoryId, updateCategoryRequestDTO);

    //Assert
    assertThat(updateCategory.getId())
        .isEqualTo(originCategory.getId());

    assertThat(updateCategory.getName())
        .isNotEqualTo(originCategory.getName());

  }


  @Test
  @DisplayName("카테고리 수정 실패 : DB에 카테고리가 존재하지 않음.")
  void updateFail() {
    // Arrange
    CategoryRequestDTO updateCategoryRequestDTO
        = makeUpdateCategoryRequestDTO();

    Category updateCategory = makeCategory();
    Long categoryId = updateCategory.getId();

    doReturn(Optional.empty())
        .when(categoryRepository)
        .findById(categoryId);

    // Act
    assertThatThrownBy(() -> {
      categoryService.update(categoryId, updateCategoryRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CategoryService.EXCEPTION_NOT_FOUND_CATEGORY);
  }


  @Test
  @DisplayName("카테고리 삭제 성공")
  void deleteSuccess() {
    // Arrange
    Category category = makeCategory();
    Long categoryId = category.getId();

    doReturn(Optional.of(category))
        .when(categoryRepository)
        .findById(categoryId);

    // Act
    categoryService.delete(categoryId);

    // Assert
    verify(categoryRepository, times(1))
        .delete(argThat(c -> c.getId().equals(category.getId())
        && c.getName().equals(category.getName())));

  }

  @Test
  @DisplayName("카테고리 삭제 실패 : DB에 존재하는 카테고리가 없음.")
  void deleteFail() {
    // Arrange
    Category category = makeCategory();
    Long categoryId = category.getId();

    doReturn(Optional.empty())
        .when(categoryRepository)
        .findById(categoryId);

    // Act, Assert
    assertThatThrownBy(() -> {
      categoryService.delete(categoryId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CategoryService.EXCEPTION_NOT_FOUND_CATEGORY);

    verify(categoryRepository, times(0))
        .delete(any(Category.class));
  }


  @Test
  @DisplayName("카테고리 조회 성공")
  void readOneByIdSuccess() {

    // Arrange
    Category category = makeCategory();
    Long categoryId = category.getId();

    doReturn(Optional.of(category)).when(categoryRepository)
        .findById(categoryId);

    // Act
    Category actual = categoryService.readOneById(categoryId);

    // Assert
    assertThat(actual).isEqualTo(category);
    assertThat(actual.getName()).isEqualTo(category.getName());
    assertThat(actual.getId()).isEqualTo(category.getId());
  }

  @Test
  @DisplayName("카테고리 조회 실패 : DB에 존재하는 카테고리가 없음")
  void readOneByIdFail() {

    // Arrange
    Category category = makeCategory();
    Long categoryId = category.getId();

    doReturn(Optional.empty()).when(categoryRepository)
        .findById(categoryId);

    // Act, Assert
    assertThatThrownBy(() -> {
      categoryService.readOneById(categoryId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CategoryService.EXCEPTION_NOT_FOUND_CATEGORY);

  }
}