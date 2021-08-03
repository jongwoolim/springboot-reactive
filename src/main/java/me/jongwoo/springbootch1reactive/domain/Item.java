package me.jongwoo.springbootch1reactive.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@Data
public class Item {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String distributorRegion;
    private Date releaseDate;
    private int availableUnits;
    private boolean active;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
