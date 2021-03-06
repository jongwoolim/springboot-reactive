package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Cart;
import me.jongwoo.springbootch1reactive.domain.CartItem;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.CartRepository;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import me.jongwoo.springbootch1reactive.service.CartService;
import me.jongwoo.springbootch1reactive.service.InventoryService;
import me.jongwoo.springbootch1reactive.service.ItemService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

//    private final CartService cartService;
//    private final ItemService itemService;
    private final InventoryService inventoryService;
//    private final ItemRepository itemRepository;
//    private final CartRepository cartRepository;

//    @GetMapping("/")
//    Mono<Rendering> home() { // <1>
//        return Mono.just(Rendering.view("home.html") // <2>
//                .modelAttribute("items", this.inventoryService.getInventory()) // <3>
//                .modelAttribute("cart", this.inventoryService.getCart("My Cart") // <4>
//                        .defaultIfEmpty(new Cart("My Cart")))
//                .build());
//    }

    @GetMapping
    Mono<Rendering> home(
                          @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                          @AuthenticationPrincipal OAuth2User oauth2User) {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items", this.inventoryService.getInventory())
                .modelAttribute("cart", this.inventoryService.getCart(cartName(oauth2User))
                        .defaultIfEmpty(new Cart(cartName(oauth2User))))

                .modelAttribute("userName", oauth2User.getName())
                .modelAttribute("authorities", oauth2User.getAuthorities())
                .modelAttribute("clientName",
                        authorizedClient.getClientRegistration().getClientName())
                .modelAttribute("userAttributes", oauth2User.getAttributes())
                .build());
    }

    private static String cartName(OAuth2User oAuth2User) {
        return oAuth2User.getName() + "'s Cart";
    }

//    @GetMapping("/")
//    Mono<Rendering> home(Authentication auth) { // <1>
//        return Mono.just(Rendering.view("home.html") // <2>
//                .modelAttribute("items", this.inventoryService.getInventory()) // <3>
//                .modelAttribute("cart", this.inventoryService.getCart(cartName(auth)) // <4>
//                        .defaultIfEmpty(new Cart(cartName(auth))))
//                .modelAttribute("auth", auth)
//                .build());
//    }

    private static String cartName(Authentication auth){
        return auth.getName() + "'s Cart";
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(Authentication auth, @PathVariable String id) {
        return this.inventoryService.addItemToCart(cartName(auth), id)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/remove/{id}")
    Mono<String> removeFromCart(Authentication auth, @PathVariable String id){
        return this.inventoryService.removeOneFromCart(cartName(auth), id)
                .thenReturn("redirect:/");
    }

    @PostMapping
    @ResponseBody
    Mono<Item> createItem(@RequestBody Item newItem) {
        return this.inventoryService.saveItem(newItem);
    }

//    @PostMapping("/")
//    Mono<String> createItem(@ModelAttribute Item newItem) {
//        return this.inventoryService.saveItem(newItem) //
//                .thenReturn("redirect:/");
//    }

//    @GetMapping("/")
//    Mono<Rendering> home(){
//        return Mono.just(Rendering.view("home.html")
//            .modelAttribute("items",
//                    this.itemRepository.findAll().doOnNext(System.out::println))
//            .modelAttribute("cart",
//                    this.cartRepository.findById("My Cart")
//                    .defaultIfEmpty(new Cart("My Cart")))
//            .build());
//    }

//    @GetMapping("/search")
//    Mono<Rendering> search(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String description,
//            @RequestParam boolean useAnd){
//
//        return Mono.just(Rendering.view("home.html")
//                .modelAttribute("results", itemService.searchByExample(name, description, useAnd))
//                .build());
//    }
//    @PostMapping("/add/{id}")
//    Mono<String> addToCart(@PathVariable String id){
//
//        return this.cartService.addToCart("My Cart", id)
//                .thenReturn("redirect:/");
//
//    }

    // ????????? ????????? ???????????? ???????????? ???????????? ???????????????
    // Mono??? ????????? ??????, Mono??? 0 ?????? 1?????? ????????? ?????? ??? ?????? ???????????? ?????????(???????????? ??????????????? ??????????????? ?????????)
//    @GetMapping("/")
//    Mono<String> home(){
//        return Mono.just("home");
//    }
}
