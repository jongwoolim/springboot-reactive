package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.Item;

public interface BlockingItemRepository extends CrudRepository<Item, String>{
}
