package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ApiIItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/api/items")
    public Flux<Item> findAll(){
        return this.itemRepository.findAll();
    }
}
