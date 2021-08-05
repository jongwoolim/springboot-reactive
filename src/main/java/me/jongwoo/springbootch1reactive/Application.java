package me.jongwoo.springbootch1reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // 해당 스레드가 블로킹 메소드 호출을 허용하는지 검사한다.
//        BlockHound.install();

        SpringApplication.run(Application.class, args);
    }



}
