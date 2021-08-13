package me.jongwoo.springbootch1reactive.controller;

import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import me.jongwoo.springbootch1reactive.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(controllers = ApiIItemController.class)
@AutoConfigureRestDocs
class ApiIItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InventoryService service;

    @MockBean
    private ItemRepository repository;

    @Test
    public void findingAllItems(){
        when(repository.findAll()).thenReturn(
                Flux.just(new Item(
                        "item-1",
                        "Alf alarm clock",
                        "nothing I really need",
                        19.99)));

        this.webTestClient.get().uri("/api/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll", preprocessResponse(prettyPrint())));

    }


}