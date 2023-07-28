package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper
public abstract class ItemMapper {
    public Item enrichWithUser(Item item, User user) {
        item.setOwner(user);
        return item;
    }

    @Mapping(source = "isAvailable", target = "available")
    public abstract ItemDto toDto(Item item);

    @Mapping(source = "available", target = "isAvailable")
    public abstract Item toItem(ItemDto itemDto);
}
