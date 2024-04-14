package org.example.dollarproduct.product.dto.response;

import lombok.Getter;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.example.dollarproduct.entity.OrderState;


@Getter
public class OrderDetailAdminResponse {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final Long price;
    private final Long quantity;
    private final Long orderId;
    private final OrderState orderState;
    private final Long userId;
    private final Long addressId;

    public OrderDetailAdminResponse(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.productId = orderDetail.getProductId();
        this.productName = orderDetail.getProductName();
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();

        Order order = orderDetail.getOrder();
        this.orderId = order.getId();
        this.orderState = order.getState();
        this.userId = order.getUserId();
        this.addressId = order.getAddressId();

    }
}
