package ru.alfabattle.promo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alfabattle.promo.dto.FinalPricePosition;
import ru.alfabattle.promo.dto.FinalPriceReceipt;
import ru.alfabattle.promo.dto.ItemGroupRule;
import ru.alfabattle.promo.dto.ItemPosition;
import ru.alfabattle.promo.dto.LoyaltyCardRule;
import ru.alfabattle.promo.dto.PromoMatrix;
import ru.alfabattle.promo.entity.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    private final InMemoryStorageService inMemoryStorageService;

    public FinalPriceReceipt calcShoppingCart(int shopId, List<ItemPosition> itemPositions, boolean loyaltyCard) {
        FinalPriceReceipt receipt = new FinalPriceReceipt();

        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        BigDecimal shopDiscount = getLoyaltyDiscountByShopId(shopId);

        for (ItemPosition itemPosition : itemPositions) {
            String itemId = itemPosition.getItemId();
            int quantity = itemPosition.getQuantity();
            Optional<Item> itemOpt = inMemoryStorageService.getItems().stream()
                    .filter(itemInList -> itemInList.getId().equals(itemId))
                    .findFirst();

            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();

                BigDecimal positionTotal;
                BigDecimal positionDiscount;
                BigDecimal positionPrice;

                if (loyaltyCard) {
                    positionPrice = item.getPrice()
                            .multiply(BigDecimal.ONE.subtract(shopDiscount));

                    positionTotal = positionPrice.multiply(BigDecimal.valueOf(quantity));

                    BigDecimal positionRegularTotal = item.getPrice()
                            .multiply(BigDecimal.valueOf(quantity));

                    positionDiscount = positionRegularTotal.subtract(positionTotal);
                } else {
                    positionPrice = item.getPrice();

                    positionTotal = item.getPrice()
                            .multiply(BigDecimal.valueOf(quantity));

                    positionDiscount = BigDecimal.ZERO;
                }

                total = total.add(positionTotal);
                discount = discount.add(positionDiscount);

                FinalPricePosition finalPricePosition = new FinalPricePosition();
                finalPricePosition.setId(itemId);
                finalPricePosition.setName(item.getName());
                finalPricePosition.setRegularPrice(item.getPrice().setScale(2, RoundingMode.HALF_EVEN));
                finalPricePosition.setPrice(positionPrice.setScale(2, RoundingMode.HALF_EVEN));

                receipt.getPositions().add(finalPricePosition);
            }
        }

        receipt.setDiscount(discount.setScale(2, RoundingMode.HALF_EVEN));
        receipt.setTotal(total.setScale(2, RoundingMode.HALF_EVEN));
        return receipt;
    }

    private BigDecimal getLoyaltyDiscountByShopId(int shopId) {
        PromoMatrix promoMatrix = inMemoryStorageService.getPromoMatrix();
        if (promoMatrix.getLoyaltyCardRules() == null) {
            return BigDecimal.ZERO;
        }
        Optional<LoyaltyCardRule> shopLoyalty = promoMatrix.getLoyaltyCardRules().stream()
                .filter(rule -> rule.getShopId() == shopId)
                .findFirst();
        if (shopLoyalty.isPresent()) {
            BigDecimal shopDiscount = BigDecimal.valueOf(shopLoyalty.get().getDiscount());
            log.info("SHOP DISCOUNT = {}", shopDiscount);
            return shopDiscount;
        }
        Optional<LoyaltyCardRule> commonLoyalty = promoMatrix.getLoyaltyCardRules().stream()
                .filter(rule -> rule.getShopId() == -1)
                .findFirst();
        if (commonLoyalty.isPresent()) {
            BigDecimal commonDiscount = BigDecimal.valueOf(commonLoyalty.get().getDiscount());
            log.info("COMMON DISCOUNT = {}", commonDiscount);
            return commonDiscount;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getGroupDiscountByShopId(int shopId, int groupId) {
        PromoMatrix promoMatrix = inMemoryStorageService.getPromoMatrix();
        if (promoMatrix.getItemGroupRules() == null) {
            return BigDecimal.ZERO;
        }
        Optional<ItemGroupRule> groupRule = promoMatrix.getItemGroupRules().stream()
                .filter(rule -> rule.getShopId() == shopId)
                .filter(rule -> rule.getShopId() == groupId)
                .findFirst();
        if (groupRule.isPresent()) {
            BigDecimal discount = BigDecimal.valueOf(groupRule.get().getDiscount());
            log.info("GROUP DISCOUNT = {}", discount);
            return discount;
        }
        Optional<ItemGroupRule> commonRule = promoMatrix.getItemGroupRules().stream()
                .filter(rule -> rule.getShopId() == -1)
                .filter(rule -> rule.getShopId() == groupId)
                .findFirst();
        if (commonRule.isPresent()) {
            BigDecimal discount = BigDecimal.valueOf(commonRule.get().getDiscount());
            log.info("COMMON GROUP DISCOUNT = {}", discount);
            return discount;
        }
        return BigDecimal.ZERO;
    }

}
