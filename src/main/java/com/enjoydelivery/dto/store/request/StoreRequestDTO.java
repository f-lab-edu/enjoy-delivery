package com.enjoydelivery.dto.store.request;

import com.enjoydelivery.entity.Store;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDTO {

  @NotNull
  private String registrationNumber;
  @NotNull
  private String name;
  @NotNull
  private String phoneNumber;
  @NotNull
  private String address;
  @NotNull
  private String thumbnailUrl;

  @NotNull
  private CategoryInfo categoryInfo;

  @NotNull
  private String description;

  @NotNull
  private String openedAt;

  @NotNull
  private String closedAt;

  @NotNull
  private int minimalOrderCost;
  @NotNull
  private int deliveryCost;

  public Store toEntity() {
    return Store.builder()
        .registrationNumber(registrationNumber)
        .name(name)
        .phoneNumber(phoneNumber)
        .address(address)
        .thumbnailUrl(thumbnailUrl)
        .category(categoryInfo.toEntity())
        .description(description)
        .openedAt(openedAt)
        .closedAt(closedAt)
        .minimalOrderCost(minimalOrderCost)
        .deliveryCost(deliveryCost)
        .build();
  }
}

