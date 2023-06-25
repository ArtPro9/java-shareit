package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Optional<Item> getItem(Integer itemId);

    Collection<Item> getAllItems(Integer userId);

    Collection<Item> searchItems(String text);

    Item addItem(Item item);

    Item editItem(Integer itemId, Item item);
}
