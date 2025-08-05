package com.sbaldasso.ecom.service;

import com.sbaldasso.ecom.dto.CartItemRequest;
import com.sbaldasso.ecom.model.CartItem;
import com.sbaldasso.ecom.model.Product;
import com.sbaldasso.ecom.model.User;
import com.sbaldasso.ecom.repository.CartItemRepository;
import com.sbaldasso.ecom.repository.ProductRepository;
import com.sbaldasso.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public Boolean addToCart(Long userId, CartItemRequest request) {
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            return false;
        }

        Product productOpt = product.get();

        if (product.get().getQuantity() < request.getQuantity()) {
            return false;
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }

        User userOpt = user.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt, productOpt);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice().multiply
                    (BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(userOpt);
            cartItem.setProduct(productOpt);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(productOpt.getPrice().multiply
                    (BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
           }
        return true;
    }

    public boolean removeFromCart(Long userId, Long id) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }

        User userOpt = user.get();

        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            return false;
        }

        CartItem cartItemOpt = cartItem.get();

        if (!cartItemOpt.getUser().equals(userOpt)) {
            return false;
        }

        cartItemRepository.delete(cartItemOpt);
        return true;
    }

    public List<CartItem> fetchCart(Long userId) {
        return userRepository.findById(userId).map(
                cartItemRepository::findByUser
        ).orElseGet(List::of);
    }
}
