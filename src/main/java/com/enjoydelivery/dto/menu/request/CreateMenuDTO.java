package com.enjoydelivery.dto.menu.request;

import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.MenuState;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuDTO {

  @NotNull
  private String name;
  @NotNull
  private int price;
  @NotNull
  private String description;
  @NotNull
  private String thumbnailUrl;
  @NotNull
  private Long storeId;

  public Menu toEntity() {
    return Menu.builder()
        .menuState(MenuState.ALIVE)
        .thumbnailUrl(this.thumbnailUrl)
        .price(this.price)
        .name(this.name)
        .description(this.description)
        .build();
  }
}
