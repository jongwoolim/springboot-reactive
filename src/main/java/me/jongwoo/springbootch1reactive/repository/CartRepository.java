package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
