package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ItemRequestMapper {

    @Mapping(source = "createTime", target = "created")
    public abstract ItemRequestDto toDto(ItemRequest itemRequest);

    @Mapping(source = "created", target = "createTime")
    public abstract ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    @Mapping(source = "isAvailable", target = "available")
    public abstract ItemDto toItemDto(Item item);

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return toItemRequest(itemRequestDto).withCreator(user);
    }

    public ItemRequestDto consItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        ItemRequestDto itemRequestDto = toDto(itemRequest);
        itemRequestDto.setItems(items.stream().map(this::toItemDto).collect(Collectors.toList()));
        return itemRequestDto;
    }
}
