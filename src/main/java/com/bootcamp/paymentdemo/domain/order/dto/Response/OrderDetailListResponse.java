package com.bootcamp.paymentdemo.domain.order.dto.Response;

import com.bootcamp.paymentdemo.domain.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderDetailListResponse {

    private final Long productId;
    private final String productName;
    private final Integer price;
    private final Integer quantity;
    private final Integer itemTotalAmount;

    public OrderDetailListResponse(
            Long productId,
            String productName,
            Integer price,
            Integer quantity,
            Integer itemTotalAmount
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.itemTotalAmount = itemTotalAmount;
    }
}