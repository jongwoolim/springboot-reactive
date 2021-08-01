package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDatabaseLoader implements ApplicationRunner {

    @Autowired
    BlockingItemRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        initialize(repository);
    }

    private void initialize(BlockingItemRepository repository) {
        repository.save(new Item("Alf alarm clock", 19.99));
        repository.save(new Item("Smurf TV tray", 19.99));
    }
}
