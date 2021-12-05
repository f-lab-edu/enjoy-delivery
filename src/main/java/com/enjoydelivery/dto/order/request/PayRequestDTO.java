package com.enjoydelivery.dto.order.request;

import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.entity.PayState;
import com.enjoydelivery.entity.PayType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayRequestDTO {

  @NotNull
  private PayType payType;

  @NotNull
  private int originCost;

  @NotNull
  private int discountCost;

  public Pay createEntity() {
    PayState state;
    if (payType.equals(PayType.바로결제)) {
      state = PayState.결제완료;
    }

    state = PayState.결제전;

    return Pay.builder()
        .payState(state)
        .payType(payType)
        .originCost(originCost)
        .discountCost(discountCost)
        .build();
  }
}
