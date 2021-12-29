package com.enjoydelivery.repository;

import com.enjoydelivery.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findAllByStore_id(Long storeId);

  List<Menu> findAllByIdIn(List<Long> menuIds);
}
