package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HypermediaItemController {

    private final ItemRepository repository;


}
