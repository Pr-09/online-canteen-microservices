package com.OnlineCanteen.OrderService.service;


import com.OnlineCanteen.OrderService.dto.MenuItem;
import com.OnlineCanteen.OrderService.dto.MenuItemResponse;
import com.OnlineCanteen.OrderService.dto.OrderItemRequest;
import com.OnlineCanteen.OrderService.dto.OrderRequest;
import com.OnlineCanteen.OrderService.entity.OrderItem;
import com.OnlineCanteen.OrderService.entity.Orders;
import com.OnlineCanteen.OrderService.repository.OrderItemRepository;
import com.OnlineCanteen.OrderService.repository.OrderRepository;
import com.OnlineCanteen.OrderService.repository.UdharCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

//    @Autowired
//    private OrderItemRepository itemRepo;

//    @Autowired
//    private UdharCodeRepository codeRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public Orders placeOrder(Long userId, String userEmail, OrderRequest request) {

        Orders order = new Orders();

        order.setUserId(userId);
        order.setUserEmail(userEmail);

        order.setTableNumber(request.getTableNumber());
        order.setPaymentType(request.getPaymentType());
        order.setCustomerName(request.getCustomerName());
        order.setEmail(request.getEmail());

        double totalAmount = 0.0;
        int estimatedTime = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {

            MenuItemResponse menuItem =
                    restTemplate.getForObject(
                            "http://MENU-SERVICE/api/menu/" + itemRequest.getMenuId(),
                            MenuItemResponse.class
                    );

            if (menuItem == null) {
                throw new RuntimeException("Menu item not found");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuId(itemRequest.getMenuId());
            orderItem.setQuantity(itemRequest.getQuantity());

            double itemTotal = menuItem.getPrice() * itemRequest.getQuantity();

            orderItem.setPrice(itemTotal);

            totalAmount += itemTotal;

            if (menuItem.isReadyMade()) {
                estimatedTime = Math.max(estimatedTime, 2);
            } else {
                estimatedTime = Math.max(estimatedTime, menuItem.getPrepTime());
            }

            orderItems.add(orderItem);
        }

        /*
         Added because payment-first is our business rule.
         Order starts as PAYMENT_PENDING.
         Future payment service will confirm it.
        */
        if ("ONLINE".equalsIgnoreCase(request.getPaymentType())) {
            order.setStatus("PAYMENT_PENDING");
        }

        /*
         Temporary OTP udhar validation.
         Future migration:
         QR approval workflow via admin service.
        */
        else if ("UDHAR".equalsIgnoreCase(request.getPaymentType())) {

            Boolean approved =
                    restTemplate.postForObject(
                            "http://ADMIN-SERVICE/api/admin/udhar/validate?userId="
                                    + userId +
                                    "&code=" +
                                    request.getUdharCode() +
                                    "&amount=" +
                                    totalAmount,
                            null,
                            Boolean.class
                    );

            if (approved == null || !approved) {
                throw new RuntimeException("Udhar approval failed");
            }

            order.setStatus("PENDING");
        }

        else {
            order.setStatus("PENDING");
        }

        order.setItems(orderItems);
        order.setEstimatedTime(estimatedTime);
        order.setTotalAmount(totalAmount);

        try {
            restTemplate.postForObject(
                    "http://NOTIFICATION-SERVICE/api/notification/order-placed?email="
                            + order.getEmail()
                            + "&estimatedTime="
                            + order.getEstimatedTime(),
                    null,
                    String.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurs in Order service in place order controller "+e.getMessage());
        }

        return orderRepository.save(order);
    }

    public List<OrderRequest> getMyOrders(Long userId) {
        List<Orders> orders=orderRepository.findByUserId(userId);
        List<OrderRequest> orderRequests = new ArrayList<>();
        for (Orders o:orders) {
            OrderRequest orderRequest=new OrderRequest();
            orderRequest.setStatus(o.getStatus());
            orderRequest.setId((o.getId()));
            orderRequest.setTotalAmount(o.getTotalAmount());
            orderRequest.setCustomerName(o.getCustomerName());
            orderRequest.setEmail(o.getEmail());
            orderRequest.setTableNumber(o.getTableNumber());
            List<OrderItem> orderItem=o.getItems();
//            orderRequest.setItems(orderItem);
            List<OrderItemRequest> orderItemRequests = new ArrayList<>();
            for (OrderItem oi: orderItem) {
                OrderItemRequest orderItemRequest=new OrderItemRequest();
                orderItemRequest.setQuantity(oi.getQuantity());
                orderItemRequest.setMenuId(oi.getMenuId());
                orderItemRequests.add(orderItemRequest);
            }
            orderRequest.setItems(orderItemRequests);
            orderRequest.setPaymentType(o.getPaymentType());

            orderRequests.add(orderRequest);
       //     orderRequest.setItems(o.getItems());
        }

        return orderRequests;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Orders updateOrderStatus(Long orderId, String status) {

        Orders order = getOrderById(orderId);

        order.setStatus(status);

        return orderRepository.save(order);
    }

    /*
     Payment service will call this after success.
    */
    @Transactional
    public Orders markPaymentSuccess(Long orderId) {

        Orders order = getOrderById(orderId);

        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    /*
     Payment failure handling.
    */
    @Transactional
    public Orders markPaymentFailed(Long orderId) {

        Orders order = getOrderById(orderId);

        order.setStatus("PAYMENT_FAILED");

        return orderRepository.save(order);
    }
}

