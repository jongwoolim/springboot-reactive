package me.jongwoo.springbootch1reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.springbootch1reactive.config.SecurityConfig;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static me.jongwoo.springbootch1reactive.config.SecurityConfig.INVENTORY;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class ApiItemController {

    private static final SimpleGrantedAuthority ROLE_INVENTORY =
                new SimpleGrantedAuthority("ROLE_" + INVENTORY);

    private final ItemRepository itemRepository;

    @GetMapping("/api/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll(Authentication auth) {
        ApiItemController controller = methodOn(ApiItemController.class);

        Mono<Link> selfLink = linkTo(controller.findAll(auth)).withSelfRel().toMono();

        Mono<Links> allLinks;

        if (auth.getAuthorities().contains(ROLE_INVENTORY)) {
            Mono<Link> addNewLink = linkTo(controller.addNewItem(null, auth)).withRel("add").toMono();

            allLinks = Mono.zip(selfLink, addNewLink) //
                    .map(links -> Links.of(links.getT1(), links.getT2()));
        } else {
            allLinks = selfLink //
                    .map(link -> Links.of(link));
        }

        return allLinks //
                .flatMap(links -> this.itemRepository.findAll() //
                        .flatMap(item -> findOne(item.getId(), auth)) //
                        .collectList() //
                        .map(entityModels -> CollectionModel.of(entityModels, links)));
    }

//    @GetMapping("/api/items")
//    public Flux<Item> findAll(){
//        return this.itemRepository.findAll();
//    }

    @GetMapping("/api/items/{id}")
    Mono<EntityModel<Item>> findOne(@PathVariable String id, Authentication auth) {
        ApiItemController controller = methodOn(ApiItemController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id, auth)).withSelfRel() //
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll(auth)) //
                .withRel(IanaLinkRelations.ITEM).toMono();

        Mono<Links> allLinks; // <1>

        if (auth.getAuthorities().contains(ROLE_INVENTORY)) { // <2>
            Mono<Link> deleteLink = linkTo(controller.deleteItem(id)).withRel("delete") //
                    .toMono();
            allLinks = Mono.zip(selfLink, aggregateLink, deleteLink) //
                    .map(links -> Links.of(links.getT1(), links.getT2(), links.getT3()));
        } else { // <3>
            allLinks = Mono.zip(selfLink, aggregateLink) //
                    .map(links -> Links.of(links.getT1(), links.getT2()));
        }

        return this.itemRepository.findById(id) //
                .zipWith(allLinks) // <4>
                .map(o -> EntityModel.of(o.getT1(), o.getT2()));
    }

    @PreAuthorize("hasRole('" + INVENTORY + "')")
    @DeleteMapping("/api/items/delete/{id}")
    Mono<ResponseEntity<?>> deleteItem(@PathVariable String id) {
        return this.itemRepository.deleteById(id) //
                .thenReturn(ResponseEntity.noContent().build());
    }

//    @GetMapping("/api/items/{id}")
//    public Mono<Item> findOne(@PathVariable String id){
//        return this.itemRepository.findById(id);
//    }


    @PreAuthorize("hasRole('" + INVENTORY + "')") // <1>
    @PostMapping("/api/items/add") // <2>
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Item item, Authentication auth) { // <3>
        return this.itemRepository.save(item) //
                .map(Item::getId) //
                .flatMap(id -> findOne(id, auth)) //
                .map(newModel -> ResponseEntity.created(
                        newModel.getRequiredLink(IanaLinkRelations.SELF) //
                        .toUri()).build());
    }

//    @PostMapping("/api/items")
//    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item){
//        return item.flatMap(s -> this.itemRepository.save(s))
//                .map(savedItem ->
//                        ResponseEntity.created(URI.create("/api/items/"+ savedItem.getId()))
//                        .body(savedItem)
//                );
//    }
    
    @PutMapping("/api/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(
            @RequestBody Mono<Item> item,
            @PathVariable String id){
        
        return item
                .map(content -> new Item(
                        id,
                        content.getName(),
                        content.getDescription(),
                        content.getPrice()))
                .flatMap(this.itemRepository::save)
                .map(ResponseEntity::ok);

    }
}
