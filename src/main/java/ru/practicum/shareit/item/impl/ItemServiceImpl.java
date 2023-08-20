package ru.practicum.shareit.item.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UnknownItemException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    public static final Sort SORT_BY_DATE = Sort.by("startTime");

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Item> getItemOptional(Integer itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Item getItem(Integer itemId) {
        Optional<Item> item = getItemOptional(itemId);
        return item.orElseThrow(() -> new UnknownItemException(itemId));
    }

    @Override
    public Collection<ItemWithBookingDto> getAllItems(Integer userId, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Illegal pagination arguments: from=" + from + ", size=" + size);
        }
        int page = from / size;
        LocalDateTime now = LocalDateTime.now();
        return itemRepository.findAllByOwnerId(userId, PageRequest.of(page, size))
                .stream()
                .map(i -> ItemMapper.consItemWithBooking(
                        i,
                        bookingRepository.findByItemId(i.getId(), SORT_BY_DATE),
                        now,
                        commentRepository.findAllByItemId(i.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Illegal pagination arguments: from=" + from + ", size=" + size);
        }
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        int page = from / size;
        return itemRepository.findAllByIsAvailableAndDescriptionContainingIgnoreCase(true, text, PageRequest.of(page, size));
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item editItem(Integer itemId, Item updatedItem) {
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Unknown item id: " + itemId);
        }
        Item item = getItemOptional(itemId).orElseThrow(() -> new IllegalArgumentException("Unknown item id: " + itemId));
        if (item.getOwner().getId() != updatedItem.getOwner().getId()) {
            throw new UnknownUserException(updatedItem.getOwner().getId());
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
        return getItemOptional(itemId).orElseThrow(() -> new IllegalArgumentException("Unknown item id: " + itemId));
    }

    @Override
    public ItemWithBookingDto getItemWithBooking(Integer itemId, Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        Item item = getItemOptional(itemId).orElseThrow(() -> new UnknownItemException(itemId));

        List<Booking> bookings = List.of();
        if (item.getOwner().getId() == userId) {
            bookings = bookingRepository.findByItemIdAndBookingStatus(item.getId(), BookingStatus.APPROVED, SORT_BY_DATE);
        }

        return ItemMapper.consItemWithBooking(item, bookings, now, commentRepository.findAllByItemId(item.getId()));
    }

    @Override
    public CommentDto addComment(Comment comment) {
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new IllegalArgumentException("Empty comment text");
        }
        List<Booking> bookings = bookingRepository.findPastBookingsForItemAndSort(
                comment.getAuthor().getId(),
                comment.getItem().getId(),
                comment.getCreateTime(),
                SORT_BY_DATE);
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("Can't create comment for userId=" + comment.getAuthor()
                    .getId() + " and itemId=" + comment.getItem().getId());
        }
        return CommentDto.consCommentDto(commentRepository.save(comment));
    }
}
