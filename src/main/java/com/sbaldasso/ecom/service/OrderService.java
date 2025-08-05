package com.sbaldasso.ecom.service;

import com.sbaldasso.ecom.dto.OrderItemDTO;
import com.sbaldasso.ecom.dto.OrderResponse;
import com.sbaldasso.ecom.model.*;
import com.sbaldasso.ecom.repository.OrderRepository;
import com.sbaldasso.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public OrderResponse createOrder(Long userId) {
        List<CartItem> cartItems = cartService.fetchCart(userId);

        if(cartItems.isEmpty()) return null;

        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) return null;

        User user = userOptional.get();

        BigDecimal totalPrice = cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> new OrderItem(
           null,
           cartItem.getProduct(),
           cartItem.getQuantity(),
           cartItem.getPrice(),
           order,
           null,
           null
        )).toList();

        order.setItems(orderItems);
        orderRepository.save(order);

        cartService.fetchCart(userId).forEach(cartItem -> cartService.removeFromCart(userId, cartItem.getId()));

        return toDTO(order);
    }

    private OrderResponse toDTO(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setItems(order.getItems().stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setId(orderItem.getId());
            orderItemDTO.setProduct(orderItem.getProduct());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setPrice(orderItem.getPrice());
            return orderItemDTO;
        }).toList());

        return orderResponse;
    }
}
