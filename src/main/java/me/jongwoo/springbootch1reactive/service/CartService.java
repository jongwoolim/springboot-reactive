package me.jongwoo.springbootch1reactive.service;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Cart;
import me.jongwoo.springbootch1reactive.domain.CartItem;
import me.jongwoo.springbootch1reactive.repository.CartRepository;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Mono<Cart> addToCart(String cartId, String id){

        return this.cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getItem()
                        .getId().equals(id))
                    .findAny()
                    .map(cartItem -> {
                        cartItem.increment();
                        return Mono.just(cart).log("newCartItem");
                    })
                .orElseGet(() ->
                    this.itemRepository.findById(id)
                            .log("fetchedItem")
                            .map(CartItem::new)
                            .log("cartItem")
                            .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                            .map(cartItem -> cart).log("addedCartItem")))
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart")
                ;

    }
}
