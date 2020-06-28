package ru.alfabattle.promo.dto;

import lombok.Data;

@Data
public class ItemCountRule {
    private int bonusQuantity; //Количество единиц товара в подарок
    private String itemId;
    private int shopId; // -1 акции сети
    private int triggerQuantity; //Количество единиц для применения скидки
}
