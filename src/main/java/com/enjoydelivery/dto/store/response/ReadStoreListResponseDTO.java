package com.enjoydelivery.dto.store.response;

import com.enjoydelivery.dto.category.response.ReadCategoryResponseDTO;
import com.enjoydelivery.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStoreListResponseDTO {
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


  public ReadStoreListResponseDTO(Store store) {
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
  }
}
