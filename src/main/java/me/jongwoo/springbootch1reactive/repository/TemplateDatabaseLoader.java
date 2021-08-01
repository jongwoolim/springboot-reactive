package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class TemplateDatabaseLoader implements ApplicationRunner {

    @Autowired
    private MongoOperations mongo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mongo.save(new Item("Alf alarm clock", 19.99));
        mongo.save(new Item("Smurf TV tray", 24.99));
    }
}
