package com.enjoydelivery.dto.store.response;

import com.enjoydelivery.dto.category.response.ReadCategoryResponseDTO;
import com.enjoydelivery.entity.Store;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStoreResponseDTO {

  private Long id;
  private String registrationNumber;
  private String name;
  private String phoneNumber;
  private String address;
  private String thumbnailUrl;
  private ReadCategoryResponseDTO category;
  private String description;
  private String openedAt;
  private String closedAt;
  private int minimalOrderCost;
  private int deliveryCost;
  private List<ReadMenuResponseDTO> menus;


  public ReadStoreResponseDTO(Store store) {
    this.id = store.getId();
    this.registrationNumber = store.getRegistrationNumber();
    this.name = store.getName();
    this.phoneNumber = store.getPhoneNumber();
    this.address = store.getAddress();
    this.thumbnailUrl = store.getThumbnailUrl();
    this.category = new ReadCategoryResponseDTO(store.getCategory());
    this.description = store.getDescription();
    this.openedAt = store.getOpenedAt();
    this.closedAt = store.getClosedAt();
    this.minimalOrderCost = store.getMinimalOrderCost();
    this.deliveryCost = store.getDeliveryCost();
    this.menus = store.getMenus().stream()
        .map(menu -> new ReadMenuResponseDTO(menu))
        .collect(Collectors.toList());

  }
}
