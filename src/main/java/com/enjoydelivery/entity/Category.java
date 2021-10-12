package com.enjoydelivery.entity;

import com.enjoydelivery.dto.request.CategoryCommand;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Category {

  @Id
  @Column(name = "category_id")
  @GeneratedValue
  private Long id;

  private String name;

  public void update(CategoryCommand categoryCommand) {
    this.name = categoryCommand.getName();
  }
}
