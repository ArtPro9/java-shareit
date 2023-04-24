package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public Item getItem(Integer itemId) {
        return itemRepository.getItemById(itemId).orElse(null);
    }

    @Override
    public Collection<Item> getAllItems(Integer userId) {
        return itemRepository.getAllItems()
                .stream()
                .filter(i -> Objects.equals(i.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.getAllItems()
                .stream()
                .filter(Item::getIsAvailable)
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item) {
        User user = userService.getUser(item.getOwnerId());
        if (user == null) {
            throw new UnknownUserException("Unknown user id: " + item);
        }
        return itemRepository.addItem(item);
    }

    @Override
    public Item editItem(Integer itemId, Item updatedItem) {
        if (!checkIfItemExists(itemId)) {
            throw new IllegalArgumentException("Unknown item id: " + itemId);
        }
        Item item = getItem(itemId);
        if (!item.getOwnerId().equals(updatedItem.getOwnerId())) {
            throw new UnknownUserException("Illegal user id: " + updatedItem);
        }
        return itemRepository.editItem(itemId, updatedItem);
    }

    private boolean checkIfItemExists(int id) {
        Optional<Item> item = itemRepository.getItemById(id);
        return item.isPresent();
    }
}
