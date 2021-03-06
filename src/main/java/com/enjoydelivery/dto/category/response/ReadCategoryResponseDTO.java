package com.enjoydelivery.dto.category.response;

import com.enjoydelivery.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadCategoryResponseDTO {
  private Long id;
  private String name;

  public ReadCategoryResponseDTO(Category category) {
    this.id = category.getId();
    this.name = category.getName();
  }
}
