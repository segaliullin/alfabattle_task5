package ru.alfabattle.promo.dto;

import lombok.Data;

@Data
public class LoyaltyCardRule {
    private double discount; //Коэффициент скидки (сидка = цена * discount)
    private int shopId; //Номер магазина, -1 для акции сети
}
