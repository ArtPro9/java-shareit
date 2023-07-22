package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
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
    public Optional<Item> getItem(Integer itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Collection<Item> getAllItems(Integer userId) {
        checkIfUserExists(userId);
        return itemRepository.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAll()
                .stream()
                .filter(Item::getIsAvailable)
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Item addItem(Item item) {
        checkIfUserExists(item.getOwnerId());
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item editItem(Integer itemId, Item updatedItem) {
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Unknown item id: " + itemId);
        }
        Item item = getItem(itemId).orElseThrow(() -> new IllegalArgumentException("Unknown item id: " + itemId));
        if (!item.getOwnerId().equals(updatedItem.getOwnerId())) {
            throw new UnknownUserException(updatedItem.getOwnerId());
        }
        if (updatedItem.getName() != null) {
            itemRepository.editItemName(itemId, updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            itemRepository.editItemDescription(itemId, updatedItem.getDescription());
        }
        if (updatedItem.getIsAvailable() != null) {
            itemRepository.editItemAvailability(itemId, updatedItem.getIsAvailable());
        }
        return getItem(itemId).orElseThrow(() -> new IllegalArgumentException("Unknown item id: " + itemId));
    }

    private void checkIfUserExists(Integer userId) {
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty()) {
            throw new UnknownUserException(userId);
        }
    }
}
