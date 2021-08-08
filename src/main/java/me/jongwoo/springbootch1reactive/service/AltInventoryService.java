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

@Service
@RequiredArgsConstructor
public class AltInventoryService {

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
        Cart myCart = this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .block();

        return myCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findAny() //
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(myCart);
                }) //
                .orElseGet(() -> this.itemRepository.findById(itemId) //
                        .map(item -> new CartItem(item)) //
                        .map(cartItem -> {
                            myCart.getCartItems().add(cartItem);
                            return myCart;
                        }))
                .flatMap(cart -> this.cartRepository.save(cart));

    }
}
