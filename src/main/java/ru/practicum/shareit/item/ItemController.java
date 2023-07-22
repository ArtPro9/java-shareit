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
import ru.practicum.shareit.exception.UnknownItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable("id") Integer itemId) {
        return itemMapper.toDto(itemService.getItem(itemId).orElseThrow(() -> new UnknownItemException(itemId)));
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItems(userId)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItem(@RequestParam("text") String text) {
        return itemService.searchItems(text)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemMapper.toDto(itemService.addItem(itemMapper.enrichWithUserId(itemMapper.toItem(itemDto), userId)));
    }

    @PatchMapping("/{id}")
    public ItemDto editItem(@PathVariable("id") Integer itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemMapper.toDto(itemService.editItem(itemId, itemMapper.enrichWithUserId(itemMapper.toItem(itemDto), userId)));
    }
}
