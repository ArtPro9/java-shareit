package ru.practicum.shareit.request.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UnknownItemRequestException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    public static final Sort SORT_BY_DATE_DESC = Sort.by("createTime").descending();
    private static final ItemRequestMapper MAPPER = Mappers.getMapper(ItemRequestMapper.class);

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public ItemRequestDto addItemRequest(ItemRequest itemRequest) {
        itemRequest.setCreateTime(LocalDateTime.now());
        return convertToDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Integer userId) {
        User user = userService.getUser(userId);
        return itemRequestRepository.findAllByCreator(user, SORT_BY_DATE_DESC).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(Integer userId, Integer requestId) {
        User user = userService.getUser(userId);
        return convertToDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new UnknownItemRequestException(requestId)));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Illegal pagination arguments: from=" + from + ", size=" + size);
        }
        int page = from / size;
        User user = userService.getUser(userId);
        return itemRequestRepository.findAllByCreatorNot(user, PageRequest.of(page, size, SORT_BY_DATE_DESC))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ItemRequestDto convertToDto(ItemRequest request) {
        return MAPPER.consItemRequestDto(request, itemRepository.findAllByRequestId(request.getId()));
    }
}
