package ru.alfabattle.promo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabattle.promo.dto.FinalPriceReceipt;
import ru.alfabattle.promo.dto.PromoMatrix;
import ru.alfabattle.promo.dto.ShoppingCart;
import ru.alfabattle.promo.service.CalculationService;
import ru.alfabattle.promo.service.InMemoryStorageService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PromoRestController {

    private final InMemoryStorageService inMemoryStorageService;
    private final CalculationService calculationService;

    @RequestMapping(method = RequestMethod.POST, value = "/promo")
    public ResponseEntity postPromoMatrix(@RequestBody PromoMatrix body) {
        log.info(" >>> promo = {}", body);
        if (body != null) {
            inMemoryStorageService.setPromoMatrix(body);
            return ResponseEntity.ok().build();
        } else {
            inMemoryStorageService.setPromoMatrix(null);
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/receipt")
    public ResponseEntity calculateReceipt(@RequestBody ShoppingCart cart) {
        log.info(" >>> cart = {}", cart);

        FinalPriceReceipt result = calculationService.calcShoppingCart(cart.getShopId(), cart.getPositions(), cart.isLoyaltyCard());
        log.info(" <<< result = {}", result);
        return ResponseEntity.ok(result);
    }


}
