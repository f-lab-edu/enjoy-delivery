package com.enjoydelivery.dto.store.response;

import com.enjoydelivery.entity.Menu;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadMenuCommand {

  @NotNull
  private Long id;
  @NotNull
  private String name;
  @NotNull
  private int price;

  public ReadMenuCommand(Menu menu) {
    this.id = menu.getId();
    this.name = menu.getName();
    this.price = menu.getPrice();
  }

  public Menu toEntity() {
    return Menu.builder()
        .id(id)
        .name(name)
        .price(price)
        .build();
  }
}