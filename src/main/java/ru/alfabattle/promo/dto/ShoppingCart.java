package ru.alfabattle.promo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingCart {
    private boolean loyaltyCard;
    private int shopId;
    private List<ItemPosition> positions;
}
