package com.enjoydelivery.dto.store.response;

import com.enjoydelivery.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadMenuCommand {

  private Long id;
  private String name;
  private int price;

  public ReadMenuCommand(Menu menu) {
    this.id = menu.getId();
    this.name = menu.getName();
    this.price = menu.getPrice();
  }
}
