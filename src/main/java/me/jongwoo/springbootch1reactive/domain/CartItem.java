package me.jongwoo.springbootch1reactive.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartItem {

    private Item item;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }
}
