package ru.practicum.shareit.item.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UnknownItemException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.CommentRepository;
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

    private static ItemWithBookingDto consItemWithBooking(Item item, List<Booking> bookings, LocalDateTime now, List<Comment> comments) {
        BookingForItemDto lastBooking = null;
        BookingForItemDto nextBooking = null;
        if (!bookings.isEmpty() && !bookings.get(0).getStartTime().isBefore(now)) {
            nextBooking = BookingForItemDto.consBookingForItemDto(bookings.get(0));
        }
        for (int i = 0; i < bookings.size() - 1; i++) {
            if (!bookings.get(i).getStartTime().isAfter(now) && !bookings.get(i + 1).getStartTime().isBefore(now)) {
                lastBooking = BookingForItemDto.consBookingForItemDto(bookings.get(i));
                nextBooking = BookingForItemDto.consBookingForItemDto(bookings.get(i + 1));
            }
        }
        if (!bookings.isEmpty() && !bookings.get(bookings.size() - 1).getStartTime().isAfter(now)) {
            lastBooking = BookingForItemDto.consBookingForItemDto(bookings.get(bookings.size() - 1));
        }

        List<CommentDto> commentDtos = comments.stream().map(CommentDto::consCommentDto).collect(Collectors.toList());

        return ItemWithBookingDto.consBookingForItemDto(item, lastBooking, nextBooking, commentDtos);
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
    public Collection<ItemWithBookingDto> getAllItems(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(i -> consItemWithBooking(
                        i,
                        bookingRepository.findByItemId(i.getId(), SORT_BY_DATE),
                        now,
                        commentRepository.findAllByItemId(i.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByIsAvailableAndDescriptionContainingIgnoreCase(true, text);
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

        return consItemWithBooking(item, bookings, now, commentRepository.findAllByItemId(item.getId()));
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
