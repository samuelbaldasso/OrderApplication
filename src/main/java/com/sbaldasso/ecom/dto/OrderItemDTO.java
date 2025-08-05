package com.sbaldasso.ecom.dto;

import com.sbaldasso.ecom.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;

    private Product product;

    private Integer quantity;

    private BigDecimal price;


}
