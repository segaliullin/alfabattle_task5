package ru.alfabattle.promo.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.alfabattle.promo.entity.Item;
import ru.alfabattle.promo.entity.ItemGroup;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitService {

    private final InMemoryStorageService inMemoryStorageService;

    @PostConstruct
    private void init() {
        List<ItemGroup> itemGroupList = loadObjectList(ItemGroup.class, "/home/ubuntu/groups.csv");
        //List<ItemGroup> itemGroupList = loadObjectList(ItemGroup.class, "classpath:groups.csv");
        inMemoryStorageService.setItemGroups(itemGroupList);

        List<Item> itemList = loadObjectList(Item.class, "/home/ubuntu/items.csv");
        //List<Item> itemList = loadObjectList(Item.class, "classpath:items.csv");
        inMemoryStorageService.setItems(itemList);
    }

    private <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = ResourceUtils.getFile(fileName);
            MappingIterator<T> readValues =
                    mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }
}
