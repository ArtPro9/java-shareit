package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new LinkedHashMap<>();
    private static Integer id = 0;

    private static int getId() {
        return ++id;
    }

    @Override
    public Optional<Item> getItemById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public Item addItem(Item item) {
        int newId = getId();
        item.setId(newId);
        items.put(newId, item);
        return item;
    }

    @Override
    public Item editItem(Integer itemId, Item updatedItem) {
        Item item = items.get(itemId);
        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getIsAvailable() != null) {
            item.setIsAvailable(updatedItem.getIsAvailable());
        }
        items.put(itemId, item);
        return item;
    }
}
