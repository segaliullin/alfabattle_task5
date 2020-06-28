package ru.alfabattle.promo.dto;

import lombok.Data;

@Data
public class ItemGroupRule {
    private double discount; //Коэффициент скидки (сидка = цена * discount)
    private String groupId;
    private int shopId; // -1 акции сети
}
