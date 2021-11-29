package com.enjoydelivery.controller;

import com.enjoydelivery.dto.store.request.StoreRequestDTO;
import com.enjoydelivery.dto.store.response.ReadStoreResponseDTO;
import com.enjoydelivery.dto.store.response.ReadStoreListResponseDTO;
import com.enjoydelivery.entity.Store;
import com.enjoydelivery.service.StoreService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

  private final StoreService storeService;

  @GetMapping("/category/{categoryId}")
  public ResponseEntity<List<ReadStoreListResponseDTO>> readByCategory(
      @PathVariable @Valid Long categoryId) {
    List<Store> stores = storeService.readAllByCategory(categoryId);

    List<ReadStoreListResponseDTO> results = stores.stream()
        .map(store -> new ReadStoreListResponseDTO(store))
        .collect(Collectors.toList());

    return new ResponseEntity<>(results, HttpStatus.OK);
  }

  @GetMapping("/{storeId}")
  public ResponseEntity read(@PathVariable @Valid Long storeId) {
    Store store = storeService.readOneFetchJoinById(storeId);

    ReadStoreResponseDTO result = new ReadStoreResponseDTO(store);

    return new ResponseEntity(result, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity create(@RequestBody @Valid StoreRequestDTO storeRequestDTO) {

    storeService.create(storeRequestDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{storeId}/edit")
  public ResponseEntity update(@PathVariable("storeId") @Valid Long storeId,
      @RequestBody @Valid StoreRequestDTO storeRequestDTO) {
    storeService.update(storeId, storeRequestDTO);
    return new ResponseEntity(HttpStatus.OK);
  }

}
