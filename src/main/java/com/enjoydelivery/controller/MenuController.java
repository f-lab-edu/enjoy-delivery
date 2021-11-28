package com.enjoydelivery.controller;

import com.enjoydelivery.dto.menu.request.CreateMenuDTO;
import com.enjoydelivery.dto.menu.request.UpdateMenuRequestDTO;
import com.enjoydelivery.dto.menu.response.MenuDTO;
import com.enjoydelivery.dto.store.response.ReadMenuResponseDTO;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.service.MenuService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

  private final MenuService menuService;

  @GetMapping("/list/{storeId}")
  public ResponseEntity<List<ReadMenuResponseDTO>> readAllByStore(
      @PathVariable("storeId") @Valid Long storeId
  ) {
    List<Menu> menus = menuService.readAllByStore(storeId);
    List<ReadMenuResponseDTO> results = menus.stream()
        .map(ReadMenuResponseDTO::new)
        .collect(Collectors.toList());

    return new ResponseEntity<>(results, HttpStatus.OK);
  }

  @GetMapping("/{menuId}")
  public ResponseEntity<MenuDTO> readOne(
      @PathVariable @Valid Long menuId
  ) {
    Menu menu = menuService.readOneById(menuId);
    MenuDTO result = new MenuDTO(menu);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PutMapping("/edit/{menuId}")
  public ResponseEntity update(
      @PathVariable @Valid Long menuId,
      @RequestBody @Valid UpdateMenuRequestDTO dto
  ) {
    menuService.update(menuId, dto);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity create(
      @RequestBody @Valid CreateMenuDTO dto) {
    menuService.create(dto);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity delete(@PathVariable @Valid Long menuId) {
    menuService.delete(menuId);
    return new ResponseEntity(HttpStatus.OK);
  }
}
