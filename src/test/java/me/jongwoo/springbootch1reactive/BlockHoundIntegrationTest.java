package me.jongwoo.springbootch1reactive;

import me.jongwoo.springbootch1reactive.domain.Cart;
import me.jongwoo.springbootch1reactive.domain.CartItem;
import me.jongwoo.springbootch1reactive.domain.Item;
import me.jongwoo.springbootch1reactive.repository.CartRepository;
import me.jongwoo.springbootch1reactive.repository.ItemRepository;
import me.jongwoo.springbootch1reactive.service.AltInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
public class BlockHoundIntegrationTest {

    AltInventoryService inventoryService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CartRepository cartRepository;

    @BeforeEach
    public void setUp(){
        //Given
        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        //when
        when(cartRepository.findById(anyString()))
                .thenReturn(Mono.<Cart> empty().hide());

        when(itemRepository.findById(anyString()))
                .thenReturn(Mono.just(sampleItem));

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(sampleCart));

        inventoryService = new AltInventoryService(itemRepository, cartRepository);
    }

    @Test
    public void blockHoundShouldTrapBlockingCall(){
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> inventoryService.addItemToCart("My Cart", "item1"))
                .as(StepVerifier::create)
                .verifyErrorSatisfies(throwable ->
                    assertThat(throwable).hasMessageContaining("block()/blockFirst()/blockLast() are blocking")
                );
    }
}
