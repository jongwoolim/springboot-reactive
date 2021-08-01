package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface BlockingItemRepository extends CrudRepository<Item, String> {
}
