package com.enjoydelivery.controller;

import com.enjoydelivery.dto.category.request.CategoryRequestDTO;
import com.enjoydelivery.dto.category.response.ReadCategoryResponseDTO;
import com.enjoydelivery.entity.Category;
import com.enjoydelivery.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<ReadCategoryResponseDTO>> readAll() {
    List<Category> categories = categoryService.readAll();

    List<ReadCategoryResponseDTO> results = categories.stream()
        .map(category -> new ReadCategoryResponseDTO(category))
        .collect(Collectors.toList());

    return new ResponseEntity<>(results, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity create(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
    categoryService.create(categoryRequestDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{categoryId}/edit")
  public ResponseEntity update(@PathVariable("categoryId") @Valid Long categoryId,
      @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {

    categoryService.update(categoryId, categoryRequestDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity delete(@PathVariable("categoryId") @Valid Long categoryId) {
    categoryService.delete(categoryId);
    return new ResponseEntity(HttpStatus.OK);
  }
}
