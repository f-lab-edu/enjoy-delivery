package com.enjoydelivery.service;

import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

  private final PayRepository payRepository;

  public void save(Pay pay) {
    payRepository.save(pay);
  }
}
