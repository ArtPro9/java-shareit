package ru.practicum.shareit.request;

import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final ItemRequestMapper MAPPER = Mappers.getMapper(ItemRequestMapper.class);
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    public ItemRequestController(ItemRequestService itemRequestService, UserService userService) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = MAPPER.toItemRequest(itemRequestDto, userService.getUser(userId));
        return itemRequestService.addItemRequest(itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer requestId) {
        return itemRequestService.getRequest(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam("from") Optional<Integer> from,
                                               @RequestParam("size") Optional<Integer> size) {
        return itemRequestService.getAllRequests(userId, from.orElse(0), size.orElse(100));
    }
}