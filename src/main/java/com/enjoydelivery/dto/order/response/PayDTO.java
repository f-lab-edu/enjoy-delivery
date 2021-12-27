package com.enjoydelivery.dto.order.response;

import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.entity.PayState;
import com.enjoydelivery.entity.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayDTO {

  private Long payId;
  private int originCost;
  private int discountCost;

  private PayState payState;
  private PayType payType;


  public static PayDTO create(Pay pay) {
    return PayDTO.builder()
        .payId(pay.getId())
        .originCost(pay.getOriginCost())
        .discountCost(pay.getDiscountCost())
        .payState(pay.getPayState())
        .payType(pay.getPayType())
        .build();
  }

  public String createNotificationBody() {
    String str = "PayDTO{" +
        "payId=" + payId +
        ", originCost=" + originCost +
        ", discountCost=" + discountCost +
        ", payState=" + payState +
        ", payType=" + payType +
        '}';

    return str;
  }

}