package me.jongwoo.springbootch1reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockHoundUnitTest {

    @Test
    public void threadSleepIsABlockingCall(){
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> {
                    try{
                        Thread.sleep(10);
                        return Mono.just(true);
                    }catch (InterruptedException e){
                        return Mono.error(e);
                    }

                })
                .as(StepVerifier::create)
//                .verifyComplete(); //테스트 코드 실패
        .verifyErrorMatches(throwable -> {
            assertThat(throwable.getMessage())
                    .contains("Blocking call! java.lang.Thread.sleep");
            return true;
        });


    }
}
