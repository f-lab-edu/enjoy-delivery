package com.enjoydelivery.dto.menu.response;

import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.MenuState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuDTO {

  private Long id;
  private String name;
  private int price;
  private String description;
  private String thumbnailUrl;
  private Long storeId;
  private MenuState menuState;

  public MenuDTO(Menu menu) {
    this.id = menu.getId();
    this.name = menu.getName();
    this.price = menu.getPrice();
    this.description = menu.getDescription();
    this.thumbnailUrl = menu.getThumbnailUrl();
    this.storeId = menu.getStore().getId();
    this.menuState = menu.getMenuState();
  }
}
