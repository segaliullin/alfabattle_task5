package ru.alfabattle.promo.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {
    private String id;
    private String name;
    private String groupId;
    private BigDecimal price;
}
