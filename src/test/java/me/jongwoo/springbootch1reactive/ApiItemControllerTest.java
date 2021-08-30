package me.jongwoo.springbootch1reactive;

import lombok.With;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ApiItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemRepository repository;

    @Autowired
    HypermediaWebTestClientConfigurer webClientConfigurer;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutateWith(webClientConfigurer);
    }

    @Test
    @WithMockUser(username = "alice", roles = {"INVENTORY"})
    void navigateToItemWithInventoryAuthority(){

        RepresentationModel<?> root = this.webTestClient.get().uri("/api") //
                .exchange() //
                .expectBody(RepresentationModel.class) //
                .returnResult().getResponseBody();

        CollectionModel<EntityModel<Item>> items = this.webTestClient.get() //
                .uri(root.getRequiredLink(IanaLinkRelations.ITEM).toUri()) //
                .exchange() //
                .expectBody(new TypeReferences.CollectionModelType<EntityModel<Item>>() {}) //
                .returnResult().getResponseBody();

        assertThat(items.getLinks()).hasSize(2);
        assertThat(items.hasLink(IanaLinkRelations.SELF)).isTrue();
        assertThat(items.hasLink("add")).isTrue();

        EntityModel<Item> first = items.getContent().iterator().next();

        EntityModel<Item> item = this.webTestClient.get() //
                .uri(first.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .exchange() //
                .expectBody(new TypeReferences.EntityModelType<Item>() {}) //
                .returnResult().getResponseBody();

        assertThat(item.getLinks()).hasSize(3);
        assertThat(item.hasLink(IanaLinkRelations.SELF)).isTrue();
        assertThat(item.hasLink(IanaLinkRelations.ITEM)).isTrue();
        assertThat(item.hasLink("delete")).isTrue();
    }

    @Test
    @WithMockUser(username = "bob", roles = { "INVENTORY" })
    void addingInventoryWithProperRoleSucceeds() {
        this.webTestClient
                .post().uri("/api/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "\"name\": \"iPhone 11\", " +
                        "\"description\": \"upgrade\", " +
                        "\"price\": 999.99" +
                        "}")
                .exchange()
                .expectStatus().isCreated();

        this.repository.findByName("iPhone 11")
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertThat(item.getDescription()).isEqualTo("upgrade");
                    assertThat(item.getPrice()).isEqualTo(999.99);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @WithMockUser(username = "alice", roles = { "SOME_OTHER_ROLE" }) // <1>
    void addingInventoryWithoutProperRoleFails() {
        this.webTestClient
                .post().uri("/api/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "\"name\": \"iPhone X\", " +
                        "\"description\": \"upgrade\", " +
                        "\"price\": 999.99" +
                        "}")
                .exchange()
                .expectStatus().isForbidden();
    }
}
