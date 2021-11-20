package com.enjoydelivery.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.enjoydelivery.dto.store.request.CategoryInfo;
import com.enjoydelivery.dto.store.request.StoreRequestDTO;
import com.enjoydelivery.entity.Category;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.MenuState;
import com.enjoydelivery.entity.Owner;
import com.enjoydelivery.entity.Store;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

  @InjectMocks
  StoreService2 storeService;

  @Mock
  StoreRepository2 storeRepository;

  @Mock
  CategoryService categoryService;

  StoreRequestDTO storeRequestDTO;

  Store store;

  Category category;

  Menu menu;

  Owner owner;

  @BeforeEach
  public void makeStore() {
    storeRequestDTO
        = new StoreRequestDTO(
        "00010030",
        "가게이름" ,
        "0320001111",
        "address",
        "thumbnailUrl",
        new CategoryInfo(1L, "한식"),
        "description",
        "12:00",
        "23:00",
        15000,
        3000
    );

    store = storeRequestDTO.toEntity();
    store.setId(1L);

    menu = Menu.builder()
        .id(1L)
        .name("menu")
        .price(15000)
        .store(store)
        .description("메뉴 설명")
        .menuState(MenuState.ALIVE)
        .thumbnailUrl("thumbnail")
        .build();

    category = Category.builder()
        .id(1L)
        .name("일식")
        .build();

    owner = Owner.builder()
        .id(1L)
        .uid("uid")
        .phoneNumber("01023923000")
        .name("owner name")
        .hashedPassword("hashedpassword")
        .email("xxx@naver.com")
        .build();
  }


  @Test
  @DisplayName("가게 등록 성공")
  void createSuccess(){

    doReturn(false)
        .when(storeRepository)
        .existsByName(store.getName());

    doReturn(store)
        .when(storeRepository)
        .save(any(Store.class));

    storeService.create(storeRequestDTO);

    verify(storeRepository, times(1))
        .existsByName(store.getName());

    verify(storeRepository, times(1))
        .save(any(Store.class));

  }

  @Test
  @DisplayName("가게 등록 실패 : 중복된 가게 이름")
  void createFail() {

    doReturn(true)
        .when(storeRepository)
        .existsByName(store.getName());

    assertThatThrownBy(() -> {
      storeService.create(storeRequestDTO);
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository, times(1))
        .existsByName(store.getName());

  }

  @Test
  @DisplayName("가게 조회 성공")
  void readOneByIdSuccess() {

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findById(store.getId());

    storeService.readOneById(store.getId());

    verify(storeRepository, times(1))
        .findById(store.getId());
  }

  @Test
  @DisplayName("가게 조회 실패 : DB에 일치하는 가게 데이터가 없음.")
  void readOneByIdFail() {

    doReturn(Optional.empty())
        .when(storeRepository)
        .findById(store.getId());

    assertThatThrownBy(() -> {
      storeService.readOneById(store.getId());
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository, times(1))
        .findById(store.getId());

  }



  @Test
  @DisplayName("가게 조회 시 카테고리, 메뉴 정보가 같이 조회됨")
  void readOneFetchJoinByIdSuccess() {
/*
  public Store readOneFetchJoinById(Long storeId) {
    Store findStore = storeRepository.findDistinctStoreFetchJoinById(storeId)
        .orElseThrow(RuntimeException::new);

    if (findStore.getCategory() == null) {
      throw new RuntimeException();
    }

    if (findStore.getMenus() == null) {
      throw new RuntimeException();
    }
    return findStore;
  }*/
    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(store.getId());

    storeService.readOneFetchJoinById(store.getId());

    verify(storeRepository,times(1))
        .findDistinctStoreFetchJoinById(store.getId());
  }

  @Test
  @DisplayName("가게, 메뉴, 카테고리 정보 조회시 조회되는 가게가 없음")
  void readOneFetchJoinByIdFail1() {

    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);

    doReturn(Optional.empty())
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(store.getId());

    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(store.getId());
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository,times(1))
        .findDistinctStoreFetchJoinById(store.getId());
  }

  @Test
  @DisplayName("가게 , 메뉴 , 카테고리 조회 시 메뉴가 조회되지 않음")
  void readOneFetchJoinByIdFail2() {

    store.setMenus(null);
    store.setCategory(category);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(store.getId());

    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(store.getId());
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository,times(1))
        .findDistinctStoreFetchJoinById(store.getId());
  }

  @Test
  @DisplayName("가게 , 메뉴 , 카테고리 조회 시 카테고리가 조회되지 않음")
  void readOneFetchJoinByIdFail3() {
    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(null);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(store.getId());

    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(store.getId());
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository,times(1))
        .findDistinctStoreFetchJoinById(store.getId());
  }

//변경 감지??
  @Test
  @DisplayName("가게 정보 변경 성공")
  void updateSuccess() {

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findById(store.getId());

    storeService.update(store.getId(), storeRequestDTO);
    store.update(storeRequestDTO);

    verify(storeRepository, times(1))
        .findById(store.getId());

  }

  @Test
  @DisplayName("가게 정보 변경 실패 : 조회되는 가게가 없음")
  void updateFail() {

    doReturn(Optional.empty())
        .when(storeRepository)
        .findById(store.getId());

    assertThatThrownBy(() -> {
      storeService.update(store.getId(), storeRequestDTO);
    }).isInstanceOf(RuntimeException.class);

  }

  @Test
  @DisplayName("카테고리별 가게 목록 조회 성공")
  void readAllByCategorySuccess() {

    store.setCategory(category);

    List<Store> stores = new ArrayList<>();
    stores.add(store);

    doReturn(category)
        .when(categoryService)
        .readOneById(category.getId());

    doReturn(stores)
        .when(storeRepository)
        .findStoreFetchJoinByCategory(any(Category.class));

    storeService.readAllByCategory(category.getId());

    verify(categoryService,times(1))
        .readOneById(category.getId());

    verify(storeRepository,times(1))
        .findStoreFetchJoinByCategory(any(Category.class));

  }


  @Test
  @DisplayName("카테고리별 가게 목록 조회 실패 : 카테고리 정보가 없음")
  void readAllByCategoryFail1() {

    doThrow(RuntimeException.class)
        .when(categoryService)
        .readOneById(category.getId());

    assertThatThrownBy(() -> {
      storeService.readAllByCategory(category.getId());
    }).isInstanceOf(RuntimeException.class);


    verify(categoryService,times(1))
        .readOneById(category.getId());
  }


  @Test
  @DisplayName("카테고리별 가게 목록 조회 실패 : 가게 정보가 없음")
  void readAllByCategoryFail2() {
    store.setCategory(category);

    doReturn(category)
        .when(categoryService)
        .readOneById(category.getId());

    doReturn(new ArrayList<>())
        .when(storeRepository)
        .findStoreFetchJoinByCategory(any(Category.class));

    assertThatThrownBy(() -> {
      storeService.readAllByCategory(category.getId());
    }).isInstanceOf(RuntimeException.class);


    verify(categoryService,times(1))
        .readOneById(category.getId());

    verify(storeRepository,times(1))
        .findStoreFetchJoinByCategory(any(Category.class));
  }


  @Test
  @DisplayName("사장님 소유 가게 조회 성공")
  void readOneByOwnerIdSuccess() {

    store.setOwner(owner);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findStoreByOwner_Id(store.getOwner().getId());

    storeService.readOneByOwnerId(1L);
  }

  @Test
  @DisplayName("사장님 소유 가게 조회 실패 : 사장님 소유 가게가 없을 때")
  void readOneByOwnerIdFail() {

    store.setOwner(owner);

    doReturn(Optional.empty())
        .when(storeRepository)
        .findStoreByOwner_Id(store.getOwner().getId());

    assertThatThrownBy(() -> {
      storeService.readOneByOwnerId(1L);
    }).isInstanceOf(RuntimeException.class);


  }

}
