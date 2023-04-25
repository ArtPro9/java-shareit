package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getItemById(Integer itemId);

    Collection<Item> getAllItems();

    Item addItem(Item item);

    Item editItem(Integer itemId, Item item);
}
