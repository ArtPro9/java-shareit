package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addItemRequest(ItemRequest itemRequest);

    List<ItemRequestDto> getUserRequests(Integer userId);

    ItemRequestDto getRequest(Integer userId, Integer requestId);

    List<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size);
}
