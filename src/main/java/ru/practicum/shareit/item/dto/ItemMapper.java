package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper
public abstract class ItemMapper {
    public Item enrichWithUserId(Item item, Integer userId) {
        if (userId != null) {
            item.setOwnerId(userId);
        }
        return item;
    }

    @Mapping(source = "isAvailable", target = "available")
    public abstract ItemDto toDto(Item item);

    @Mapping(source = "available", target = "isAvailable")
    public abstract Item toItem(ItemDto itemDto);
}
