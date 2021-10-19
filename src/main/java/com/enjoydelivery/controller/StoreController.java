package com.enjoydelivery.controller;

import com.enjoydelivery.dto.store.request.StoreCommand;
import com.enjoydelivery.dto.store.response.ReadStoreCommand;
import com.enjoydelivery.dto.store.response.ReadStoreListCommand;
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
  public ResponseEntity<List<ReadStoreListCommand>> readByCategory(
      @PathVariable @Valid Long categoryId) {
    List<Store> stores = storeService.readAllByCategory(categoryId);

    List<ReadStoreListCommand> results = stores.stream()
        .map(store -> new ReadStoreListCommand(store))
        .collect(Collectors.toList());

    return new ResponseEntity<>(results, HttpStatus.OK);
  }

  @GetMapping("/{storeId}")
  public ResponseEntity read(@PathVariable @Valid Long storeId) {
    Store store = storeService.readOneFetchJoinById(storeId);

    ReadStoreCommand result = new ReadStoreCommand(store);

    return new ResponseEntity(result, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity create(@RequestBody @Valid StoreCommand storeCommand) {

    storeService.create(storeCommand);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{storeId}/edit")
  public ResponseEntity update(@PathVariable("storeId") @Valid Long storeId,
      @RequestBody @Valid StoreCommand storeCommand) {
    storeService.update(storeId, storeCommand);
    return new ResponseEntity(HttpStatus.OK);
  }

}
