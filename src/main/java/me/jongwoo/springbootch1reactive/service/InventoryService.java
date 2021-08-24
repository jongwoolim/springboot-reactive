package me.jongwoo.springbootch1reactive.service;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Cart;
import me.jongwoo.springbootch1reactive.domain.CartItem;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.CartRepository;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;

    private final CartRepository cartRepository;


    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    public Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {

        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId)) //
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        }) //
                        .orElseGet(() -> {
                            return this.itemRepository.findById(itemId) //
                                    .map(item -> new CartItem(item)) //
                                    .map(cartItem -> {
                                        cart.getCartItems().add(cartItem);
                                        return cart;
                                    });
                        }))
                .flatMap(cart -> this.cartRepository.save(cart));
    }

    public Mono<Cart> removeOneFromCart(String cartId, String itemId) {

        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                    .findAny()
                .map(cartItem -> {
                    cartItem.decrement();
                    return Mono.just(cart);
                })
                        .orElse(Mono.empty()))
                .map(cart -> new Cart(cart.getId(), cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getQuantity() > 0).collect(Collectors.toList())))
                .flatMap(cart -> this.cartRepository.save(cart));

    }
}
