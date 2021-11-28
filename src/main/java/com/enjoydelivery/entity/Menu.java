package com.enjoydelivery.entity;


import com.enjoydelivery.dto.menu.request.UpdateMenuRequestDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

  @Id
  @Column(name = "menu_id")
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
  private String description;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @Enumerated(EnumType.STRING)
  private MenuState menuState;

  public void update(UpdateMenuRequestDTO dto) {
    this.name = dto.getName();
    this.price = dto.getPrice();
    this.description = dto.getDescription();
    this.thumbnailUrl = dto.getThumbnailUrl();
    this.menuState = dto.getMenuState();
  }

  public void updateDeleteState() {
    this.menuState = MenuState.DELETED;
  }
}
