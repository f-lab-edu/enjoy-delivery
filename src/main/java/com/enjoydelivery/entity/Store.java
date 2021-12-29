package com.enjoydelivery.entity;

import com.enjoydelivery.dto.store.request.StoreRequestDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class Store {
  @Id
  @Column(name = "store_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="owner_id")
  private Owner owner;

  @Column(name = "registration_number")
  private String registrationNumber;

  private String name;

  @Column(name = "phone_number")
  private String phoneNumber;

  private String address;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  private String description;

  @Column(name = "opened_at")
  private String openedAt;

  @Column(name = "closed_at")
  private String closedAt;

  @Column(name = "minimal_order_cost")
  private int minimalOrderCost;

  @Column(name = "delivery_cost")
  private int deliveryCost;

  private String coordinate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "store")
  private List<Menu> menus = new ArrayList<>();

  public void update(StoreRequestDTO storeRequestDTO) {
    this.registrationNumber = storeRequestDTO.getRegistrationNumber();
    this.name = storeRequestDTO.getName();
    this.phoneNumber = storeRequestDTO.getPhoneNumber();
    this.address = storeRequestDTO.getAddress();
    this.thumbnailUrl = storeRequestDTO.getThumbnailUrl();
    this.category = storeRequestDTO.getCategoryInfo().toEntity();
    this.description = storeRequestDTO.getDescription();
    this.openedAt = storeRequestDTO.getOpenedAt();
    this.closedAt = storeRequestDTO.getClosedAt();
    this.minimalOrderCost = storeRequestDTO.getMinimalOrderCost();
    this.deliveryCost = storeRequestDTO.getDeliveryCost();
  }
}
