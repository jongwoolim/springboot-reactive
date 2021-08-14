package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class HypermediaItemController {

    private final ItemRepository repository;

    @GetMapping("/hypermedia/items/{id}")
    public Mono<EntityModel<Item>> findOne(@PathVariable String id){
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        final Mono<Link> selfLink = linkTo(controller.findOne(id)).withSelfRel().toMono();

        final Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM).toMono();

        return Mono.zip(repository.findById(id), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    // tag::find-all[]
    @GetMapping("/hypermedia/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {

        return this.repository.findAll() //
                .flatMap(item -> findOne(item.getId())) //
                .collectList() //
                .flatMap(entityModels -> linkTo(methodOn(HypermediaItemController.class) //
                        .findAll()).withSelfRel() //
                        .toMono()
                        .map(selfLink -> CollectionModel.of(entityModels, selfLink)));
    }
    // end::find-all[]
}
