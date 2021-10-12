package com.enjoydelivery.dto.request;

import com.enjoydelivery.entity.Category;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCommand {

  @NotNull
  private String name;

  public Category toEntity() {
    return Category.builder()
        .name(name)
        .build();
  }
}
