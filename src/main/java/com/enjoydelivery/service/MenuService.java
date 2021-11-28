package com.enjoydelivery.service;

import com.enjoydelivery.dto.menu.request.CreateMenuDTO;
import com.enjoydelivery.dto.menu.request.UpdateMenuRequestDTO;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.Store;
import com.enjoydelivery.repository.MenuRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

  public static final String EXCEPTION_NOT_FOUND_MENU
      = "조회되는 메뉴 데이터가 없습니다.";
  private final MenuRepository menuRepository;
  private final StoreService storeService;

  public List<Menu> readAllByStore(Long storeId) {
    List<Menu> menus = menuRepository.findAllByStore_id(storeId);
    if (menus == null || menus.isEmpty()) {
      throw new RuntimeException(EXCEPTION_NOT_FOUND_MENU);
    }
    return menus;
  }

  public Menu readOneById(Long menuId) {
    return menuRepository.findById(menuId)
        .orElseThrow(() -> new RuntimeException(EXCEPTION_NOT_FOUND_MENU));
  }

  @Transactional
  public void update(Long menuId, UpdateMenuRequestDTO dto) {
    Menu findMenu = readOneById(menuId);
    findMenu.update(dto);
  }

  public void create(CreateMenuDTO dto) {

    Long storeId = dto.getStoreId();
    Store store = storeService.readOneById(storeId);

    Menu menu = dto.toEntity();
    menu.setStore(store);

    menuRepository.save(menu);
  }

  @Transactional
  public void delete(Long menuId) {
    Menu menu = readOneById(menuId);
    menu.updateDeleteState();
  }
}
