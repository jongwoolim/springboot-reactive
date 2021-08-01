package me.jongwoo.springbootch1reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Dish {

    private String description;
    private boolean delivered = false;

    public static Dish deliver(Dish dish){
        Dish deliveredDish = new Dish(dish.getDescription());
        deliveredDish.setDelivered(true);
        return deliveredDish;
    }

    public Dish(String description){
        this.description = description;
    }
}
