package com.enjoydelivery.dto.menu.request;

import com.enjoydelivery.entity.MenuState;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuRequestDTO {

  @NotNull
  private String name;
  @NotNull
  private int price;
  @NotNull
  private String description;
  @NotNull
  private String thumbnailUrl;
  @NotNull
  private MenuState menuState;
}
