package com.sbaldasso.ecom.repository;

import com.sbaldasso.ecom.model.CartItem;
import com.sbaldasso.ecom.model.Product;
import com.sbaldasso.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);
    List<CartItem> findByUser(User user);
}

