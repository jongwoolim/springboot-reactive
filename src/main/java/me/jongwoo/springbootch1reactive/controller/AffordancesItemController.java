package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mediatype.alps.Alps.alps;
import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class AffordancesItemController {

    private final ItemRepository repository;

    // tag::root[]
    @GetMapping("/affordances")
    Mono<RepresentationModel<?>> root() {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> selfLink = linkTo(controller.root()) //
                .withSelfRel() //
                .toMono();

        Mono<Link> itemsAggregateLink = linkTo(controller.findAll()) //
                .withRel(IanaLinkRelations.ITEM) //
                .toMono();

        return selfLink.zipWith(itemsAggregateLink) //
                .map(links -> Links.of(links.getT1(), links.getT2())) //
                .map(links -> new RepresentationModel<>(links.toList()));
    }
    // end::root[]

    // tag::find-all[]
    @GetMapping("/affordances/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> aggregateRoot = linkTo(controller.findAll()) //
                .withSelfRel() //
                .andAffordance(controller.addNewItem(null)) // <1>
                .toMono();

        return this.repository.findAll() // <2>
                .flatMap(item -> findOne(item.getId())) // <3>
                .collectList() // <4>
                .flatMap(models -> aggregateRoot //
                        .map(selfLink -> CollectionModel.of( //
                                models, selfLink))); // <5>
    }
    // end::find-all[]

    // tag::find-one[]
    @GetMapping("/affordances/items/{id}") // <1>
    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        AffordancesItemController controller = methodOn(AffordancesItemController.class); // <2>

        Mono<Link> selfLink = linkTo(controller.findOne(id)) //
                .withSelfRel() //
                .andAffordance(controller.updateItem(null, id)) // <3>
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll()) //
                .withRel(IanaLinkRelations.ITEM) //
                .toMono();

        return Mono.zip(repository.findById(id), selfLink, aggregateLink) //
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }
    // end::find-one[]

    // tag::add-new-item[]
    @PostMapping("/affordances/items") // <1>
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<EntityModel<Item>> item) { // <2>
        return item //
                .map(EntityModel::getContent) // <3>
                .flatMap(this.repository::save) // <4>
                .map(Item::getId) // <5>
                .flatMap(this::findOne) // <6>
                .map(newModel -> ResponseEntity.created(newModel // <7>
                        .getRequiredLink(IanaLinkRelations.SELF) //
                        .toUri()).body(newModel.getContent()));
    }
    // end::add-new-item[]

    @GetMapping("/affordances/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<EntityModel<Item>> item,
                                              @PathVariable String id
                                              ){
        return item
                .map(EntityModel::getContent)
                .map(content -> new Item(id, content.getName(),
                        content.getDescription(), content.getPrice()))
                .flatMap(this.repository::save)
                .then(findOne(id))
                .map(model -> ResponseEntity.noContent()
                        .location(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());

    }

    // tag::profile[]
    @GetMapping(value = "/affordances/items/profile", produces = MediaTypes.ALPS_JSON_VALUE)
    public Alps profile() {
        return alps() //
                .descriptor(Collections.singletonList(descriptor() //
                        .id(Item.class.getSimpleName() + "-representation") //
                        .descriptor( //
                                Arrays.stream(Item.class.getDeclaredFields()) //
                                        .map(field -> descriptor() //
                                                .name(field.getName()) //
                                                .type(Type.SEMANTIC) //
                                                .build()) //
                                        .collect(Collectors.toList())) //
                        .build())) //
                .build();
    }
    // end::profile[]
}
