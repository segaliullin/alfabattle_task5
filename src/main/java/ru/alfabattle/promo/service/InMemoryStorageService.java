package ru.alfabattle.promo.service;

import org.springframework.stereotype.Service;
import ru.alfabattle.promo.dto.PromoMatrix;
import ru.alfabattle.promo.entity.Item;
import ru.alfabattle.promo.entity.ItemGroup;

import java.util.List;

@Service
public class InMemoryStorageService {

    private List<Item> items;
    private List<ItemGroup> itemGroups;
    private PromoMatrix promoMatrix;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(List<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }

    public PromoMatrix getPromoMatrix() {
        return promoMatrix;
    }

    public void setPromoMatrix(PromoMatrix promoMatrix) {
        this.promoMatrix = promoMatrix;
    }
}
