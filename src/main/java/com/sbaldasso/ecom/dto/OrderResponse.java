package com.sbaldasso.ecom.dto;

import com.sbaldasso.ecom.model.OrderItem;
import com.sbaldasso.ecom.model.OrderStatus;
import com.sbaldasso.ecom.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<OrderItemDTO> items;

    private LocalDateTime createdAt;
}
