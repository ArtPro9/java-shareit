package ru.practicum.shareit.item;

import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);
    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ItemWithBookingDto getItem(@PathVariable("id") Integer itemId,
                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItemWithBooking(itemId, userId);
    }

    @GetMapping
    public Collection<ItemWithBookingDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItem(@RequestParam("text") String text) {
        return itemService.searchItems(text)
                .stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return MAPPER.toDto(itemService.addItem(MAPPER.toItem(itemDto, userService.getUser(userId))));
    }

    @PatchMapping("/{id}")
    public ItemDto editItem(@PathVariable("id") Integer itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return MAPPER.toDto(itemService.editItem(itemId, MAPPER.toItem(itemDto, userService.getUser(userId))));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") Integer itemId, @RequestBody Comment comment, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        comment.setAuthor(userService.getUser(userId));
        comment.setItem(itemService.getItem(itemId));
        comment.setCreateTime(LocalDateTime.now());
        return itemService.addComment(comment);
    }
}
