package com.OnlineCanteen.OrderService.controller;

import com.OnlineCanteen.OrderService.dto.OrderStatusUpdateRequest;
import com.OnlineCanteen.OrderService.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.OnlineCanteen.OrderService.dto.OrderRequest;
import com.OnlineCanteen.OrderService.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /*
      USER places order
    */
    @PostMapping
    public Orders placeOrder(
            @RequestHeader("x-user-id") Long userId,
            @RequestHeader("x-user-email") String userEmail,
            @RequestBody OrderRequest request
    ) {
        return orderService.placeOrder(userId, userEmail, request);
    }

    /*
      USER order history
    */
    @GetMapping("/my-orders")
    public List<OrderRequest> getMyOrders(
            @RequestHeader("x-user-id") Long userId
    ) {
        return orderService.getMyOrders(userId);
    }

    /*
      ADMIN all orders
    */
    @GetMapping("/all")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    /*
      ADMIN single order details
    */
    @GetMapping("/{orderId}")
    public Orders getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    /*
      ADMIN updates order lifecycle
    */
    @PutMapping("/{orderId}/status")
    public Orders updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        return orderService.updateOrderStatus(orderId, request.getStatus());
    }

    /*
      PAYMENT SERVICE callback
    */
    @PutMapping("/{orderId}/payment-success")
    public Orders paymentSuccess(@PathVariable Long orderId) {
        return orderService.markPaymentSuccess(orderId);
    }

    /*
      PAYMENT SERVICE callback
    */
    @PutMapping("/{orderId}/payment-failed")
    public Orders paymentFailed(@PathVariable Long orderId) {
        return orderService.markPaymentFailed(orderId);
    }
}
