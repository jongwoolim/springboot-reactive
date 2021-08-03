package me.jongwoo.springbootch1reactive.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemByExampleRepository;
import me.jongwoo.springbootch1reactive.repository.ItemByFluentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemByExampleRepository exampleRepository;
    private final ItemByFluentRepository fluentRepository;

    public Flux<Item> searchByExample(String name, String description, boolean useAnd){

        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd
                ? ExampleMatcher.matchingAll()
                : ExampleMatcher.matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);
        return exampleRepository.findAll(probe);
    }

//    public Flux<Item> searchByFluentExample(String name, String description, boolean useAnd){
//        Item item = new Item(name, description, 0.0.);
//
//        ExampleMatcher matcher = (useAnd
//                ? ExampleMatcher.matchingAll()
//                : ExampleMatcher.matchingAny())
//                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
//                .withIgnoreCase()
//                .withIgnorePaths("price");
//        return fluentRepository.query(Item.class)
//                .matching(query(Example.of(item, matcher)))
//    }

}
