package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Optional<Item> getItemOptional(Integer itemId);

    Item getItem(Integer itemId);

    Collection<ItemWithBookingDto> getAllItems(Integer userId);

    Collection<Item> searchItems(String text);

    Item addItem(Item item);

    Item editItem(Integer itemId, Item item);

    ItemWithBookingDto getItemWithBooking(Integer itemId, Integer userId);

    CommentDto addComment(Comment comment);
}
