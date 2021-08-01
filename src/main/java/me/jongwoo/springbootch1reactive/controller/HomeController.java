package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Cart;
import me.jongwoo.springbootch1reactive.repository.CartRepository;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @GetMapping("/")
    Mono<Rendering> home(){
        return Mono.just(Rendering.view("home.html")
            .modelAttribute("items",
                    this.itemRepository.findAll())
            .modelAttribute("cart",
                    this.cartRepository.findById("My Cart")
                    .defaultIfEmpty(new Cart("My Cart")))
            .build());
    }

    // 템플릿 이름을 나타내는 문자열을 리액티브 컨테이너인
    // Mono에 담아서 반환, Mono는 0 또는 1개의 원소만 담을 수 있는 리액티브 발행자(프로젝트 리액터에서 제공해주는 구현체)
//    @GetMapping("/")
//    Mono<String> home(){
//        return Mono.just("home");
//    }
}
