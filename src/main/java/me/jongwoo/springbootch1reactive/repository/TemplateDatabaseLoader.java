package me.jongwoo.springbootch1reactive.repository;

import lombok.extern.slf4j.Slf4j;
import me.jongwoo.springbootch1reactive.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemplateDatabaseLoader implements ApplicationRunner {

    @Autowired
    private MongoOperations mongo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mongo.save(new Item("Alf alarm clock", 19.99));
        mongo.save(new Item("Smurf TV tray", 24.99));
        log.info("=====================================>");
        log.info("=====================================>");
        log.info("=====================================>");
        log.info("=====================================>");
    }
}
