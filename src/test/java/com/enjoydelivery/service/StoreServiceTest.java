package com.enjoydelivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
import com.enjoydelivery.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

  @InjectMocks
  StoreService storeService;

  @Mock
  StoreRepository storeRepository;

  @Mock
  CategoryService categoryService;

  public StoreRequestDTO makeStoreRequestDTO() {
    return new StoreRequestDTO(
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
  }


  private StoreRequestDTO makeUpdateStoreReqeustDTO() {
    return new StoreRequestDTO(
        "00010031",//update
        "수정된가게이름" ,//update
        "0320001111",
        "updateAddress",//update
        "updateThumbnailUrl",//update
        new CategoryInfo(1L, "한식"),
        "updateDescription",
        "12:00",
        "23:00",
        15000,
        3000
    );
  }

  public Store makeStore() {
    return makeStore(makeStoreRequestDTO());
  }

  public Store makeStore(StoreRequestDTO storeRequestDTO) {
    Store store = storeRequestDTO.toEntity();
    store.setId(1L);
    return store;
  }


  public Menu makeMenu() {
    return Menu.builder()
        .id(1L)
        .name("menu")
        .price(15000)
        .store(makeStore())
        .description("메뉴 설명")
        .menuState(MenuState.ALIVE)
        .thumbnailUrl("thumbnail")
        .build();
  }

  public Category makeCategory() {
    return Category.builder()
        .id(1L)
        .name("일식")
        .build();
  }

  public Owner makeOwner() {
    return Owner.builder()
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

    //Arrange
    StoreRequestDTO storeRequestDTO = makeStoreRequestDTO();
    String storeName = storeRequestDTO.getName();
    Store store = makeStore(storeRequestDTO);

    doReturn(false)
        .when(storeRepository)
        .existsByName(storeName);

    doReturn(store)
        .when(storeRepository)
        .save(any(Store.class));

    //Act
    storeService.create(storeRequestDTO);

    //Assert
    verify(storeRepository, times(1))
        .save(argThat(s -> s.getName().equals(storeRequestDTO.getName())
            && s.getAddress().equals(storeRequestDTO.getAddress())
        && s.getPhoneNumber().equals(storeRequestDTO.getPhoneNumber())
        && s.getRegistrationNumber().equals(storeRequestDTO.getRegistrationNumber())));
  }


  @Test
  @DisplayName("가게 등록 실패 : 중복된 가게 이름")
  void createFail() {

    //Arrange
    StoreRequestDTO storeRequestDTO = makeStoreRequestDTO();
    String storeName = storeRequestDTO.getName();

    doReturn(true)
        .when(storeRepository)
        .existsByName(storeName);

    //Act
    assertThatThrownBy(() -> {
      storeService.create(storeRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_DUPLICATE_NAME);

    //Assert
    verify(storeRepository, times(0))
        .save(any(Store.class));

  }

  @Test
  @DisplayName("가게 조회 성공")
  void readOneByIdSuccess() {

    // Arrange
    Store store = makeStore();
    Owner owner = makeOwner();
    Menu menu = makeMenu();
    Category category = makeCategory();
    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);
    store.setOwner(owner);

    Long storeId = store.getId();

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findById(storeId);

    // Act
    Store actual = storeService.readOneById(storeId);

    //Assert
    assertThat(store).isEqualTo(actual);
  }

  @Test
  @DisplayName("가게 조회 실패 : DB에 일치하는 가게 데이터가 없음.")
  void readOneByIdFail() {

    // Arrange
    Store store = makeStore();
    Owner owner = makeOwner();
    Menu menu = makeMenu();
    Category category = makeCategory();
    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);
    store.setOwner(owner);

    Long storeId = store.getId();

    doReturn(Optional.empty())
        .when(storeRepository)
        .findById(storeId);

    //Act , Assert
    assertThatThrownBy(() -> {
      storeService.readOneById(storeId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_STORE);

  }



  @Test
  @DisplayName("가게 조회 시 카테고리, 메뉴 정보가 같이 조회됨")
  void readOneFetchJoinByIdSuccess() {

    // Arrange
    Store store = makeStore();
    Long storeId = store.getId();
    Menu menu = makeMenu();

    Category category = makeCategory();

    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(storeId);

    // Act
    Store actual = storeService
        .readOneFetchJoinById(storeId);

    // Assert
    assertThat(actual.getMenus()).isNotNull();
    assertThat(actual.getCategory()).isNotNull();
    assertThat(actual.getOwner()).isNull();

    assertThat(actual).isEqualTo(store);
  }

  @Test
  @DisplayName("가게, 메뉴, 카테고리 정보 조회시 조회되는 가게가 없음")
  void readOneFetchJoinByIdFail1() {

    // Arrange
    Store store = makeStore();
    Long storeId = store.getId();
    Menu menu = makeMenu();
    Category category = makeCategory();

    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(category);

    doReturn(Optional.empty())
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(storeId);

    // Act, Assert
    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(storeId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_STORE);


  }

  @Test
  @DisplayName("가게 , 메뉴 , 카테고리 조회 시 메뉴가 조회되지 않음")
  void readOneFetchJoinByIdFail2() {

    Store store = makeStore();
    Long storeId = store.getId();
    Category category = makeCategory();

    store.setCategory(category);
    store.setMenus(null);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(storeId);

    // Act , Assert
    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(storeId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_MENUS);


  }

  @Test
  @DisplayName("가게 , 메뉴 , 카테고리 조회 시 카테고리가 조회되지 않음")
  void readOneFetchJoinByIdFail3() {

    Menu menu = makeMenu();
    Store store = makeStore();
    Long storeId = store.getId();

    List<Menu> menus = new ArrayList<>();
    menus.add(menu);

    store.setMenus(menus);
    store.setCategory(null);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findDistinctStoreFetchJoinById(storeId);

    assertThatThrownBy(() -> {
      storeService.readOneFetchJoinById(storeId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_CATEGORY);

  }


  @Test
  @DisplayName("가게 정보 변경 성공")
  void updateSuccess() {

    // Arrange
    Store beforeStore = makeStore();

    Store updateStore = makeStore();
    Long storeId = updateStore.getId();
    StoreRequestDTO updateStoreRequestDTO
        = makeUpdateStoreReqeustDTO();

    doReturn(Optional.of(updateStore))
        .when(storeRepository)
        .findById(storeId);

    // Act
    storeService.update(storeId, updateStoreRequestDTO);

    //Assert
    assertThat(updateStore.getId())
        .isEqualTo(beforeStore.getId());

    assertThat(updateStore.getRegistrationNumber())
        .isNotEqualTo(beforeStore.getRegistrationNumber());

    assertThat(updateStore.getName())
        .isNotEqualTo(beforeStore.getName());

    assertThat(updateStore.getAddress())
        .isNotEqualTo(beforeStore.getAddress());

    assertThat(updateStore.getThumbnailUrl())
        .isNotEqualTo(beforeStore.getThumbnailUrl());


  }


  @Test
  @DisplayName("가게 정보 변경 실패 : 조회되는 가게가 없음")
  void updateFail() {

    StoreRequestDTO storeRequestDTO = makeStoreRequestDTO();
    Store store = makeStore(storeRequestDTO);
    Long storeId = store.getId();

    doReturn(Optional.empty())
        .when(storeRepository)
        .findById(storeId);

    assertThatThrownBy(() -> {
      storeService.update(storeId, storeRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_STORE);



  }

  @Test
  @DisplayName("카테고리별 가게 목록 조회 성공")
  void readAllByCategorySuccess() {

    // Arrange
    Category category = makeCategory();
    Long categoryId = category.getId();
    Store store = makeStore();
    store.setCategory(category);

    List<Store> stores = new ArrayList<>();
    stores.add(store);

    doReturn(category)
        .when(categoryService)
        .readOneById(categoryId);

    doReturn(stores)
        .when(storeRepository)
        .findStoreFetchJoinByCategory(any(Category.class));

    // Act
    List<Store> actualStores = storeService
        .readAllByCategory(categoryId);

    // Assert
    assertThat(actualStores.size()).isSameAs(stores.size());
    assertThat(actualStores).isEqualTo(stores);
    assertThat(actualStores.get(0)).isEqualTo(stores.get(0));
  }


  @Test
  @DisplayName("카테고리별 가게 목록 조회 실패 : 카테고리 정보가 없음")
  void readAllByCategoryFail1() {

    Category category = makeCategory();
    Store store = makeStore();
    Long categoryId = category.getId();

    doThrow(RuntimeException.class)
        .when(categoryService)
        .readOneById(categoryId);

    assertThatThrownBy(() -> {
      storeService.readAllByCategory(categoryId);
    }).isInstanceOf(RuntimeException.class);

    verify(storeRepository, times(0))
        .findStoreFetchJoinByCategory(any(Category.class));

  }


  @Test
  @DisplayName("카테고리별 가게 목록 조회 실패 : 가게 정보가 없음")
  void readAllByCategoryFail2() {

    Category category = makeCategory();
    Store store = makeStore();

    store.setCategory(category);

    doReturn(category)
        .when(categoryService)
        .readOneById(category.getId());

    doReturn(new ArrayList<>())
        .when(storeRepository)
        .findStoreFetchJoinByCategory(any(Category.class));

    assertThatThrownBy(() -> {
      storeService.readAllByCategory(category.getId());
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_STORE);

  }


  @Test
  @DisplayName("사장님 소유 가게 조회 성공")
  void readOneByOwnerIdSuccess() {

    // Arrange
    Owner owner = makeOwner();
    Long ownerId = owner.getId();
    Store store = makeStore();
    store.setOwner(owner);

    doReturn(Optional.of(store))
        .when(storeRepository)
        .findStoreByOwner_Id(ownerId);

    // Act
    Store actual = storeService.readOneByOwnerId(ownerId);

    // Assert
    assertThat(actual).isEqualTo(store);
    assertThat(actual.getOwner()).isEqualTo(owner);
  }

  @Test
  @DisplayName("사장님 소유 가게 조회 실패 : 사장님 소유 가게가 없을 때")
  void readOneByOwnerIdFail() {

    Owner owner = makeOwner();
    Store store = makeStore();
    Long ownerId = owner.getId();

    store.setOwner(owner);

    doReturn(Optional.empty())
        .when(storeRepository)
        .findStoreByOwner_Id(ownerId);

    assertThatThrownBy(() -> {
      storeService.readOneByOwnerId(ownerId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(StoreService.EXCEPTION_NOT_FOUND_STORE);

  }

}
