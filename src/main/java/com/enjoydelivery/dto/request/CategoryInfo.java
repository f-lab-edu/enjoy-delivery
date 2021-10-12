package com.enjoydelivery.dto.request;

import com.enjoydelivery.entity.Category;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfo {
  @NotNull
  private Long id;
  @NotNull
  private String name;

  public Category toEntity() {
    return Category.builder()
        .id(id)
        .name(name)
        .build();
  }
}
