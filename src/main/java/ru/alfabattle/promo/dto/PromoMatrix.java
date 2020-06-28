package ru.alfabattle.promo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromoMatrix {
    private List<ItemCountRule> itemCountRules;
    private List<ItemGroupRule> itemGroupRules;
    private List<LoyaltyCardRule> loyaltyCardRules;
}
