package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, String>{
}
