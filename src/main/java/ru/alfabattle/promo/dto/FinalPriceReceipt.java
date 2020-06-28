package ru.alfabattle.promo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class FinalPriceReceipt {
    private BigDecimal total;
    private BigDecimal discount;
    private List<FinalPricePosition> positions = new ArrayList<>();
}
