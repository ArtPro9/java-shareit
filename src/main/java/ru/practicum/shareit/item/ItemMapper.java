package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public abstract class ItemMapper {
    public static ItemWithBookingDto consItemWithBooking(Item item, List<Booking> bookings, LocalDateTime now, List<Comment> comments) {
        BookingForItemDto lastBooking = null;
        BookingForItemDto nextBooking = null;
        if (!bookings.isEmpty() && !bookings.get(0).getStartTime().isBefore(now)) {
            nextBooking = BookingMapper.consBookingForItemDto(bookings.get(0));
        }
        for (int i = 0; i < bookings.size() - 1; i++) {
            if (!bookings.get(i).getStartTime().isAfter(now) && !bookings.get(i + 1).getStartTime().isBefore(now)) {
                lastBooking = BookingMapper.consBookingForItemDto(bookings.get(i));
                nextBooking = BookingMapper.consBookingForItemDto(bookings.get(i + 1));
            }
        }
        if (!bookings.isEmpty() && !bookings.get(bookings.size() - 1).getStartTime().isAfter(now)) {
            lastBooking = BookingMapper.consBookingForItemDto(bookings.get(bookings.size() - 1));
        }

        List<CommentDto> commentDtos = comments.stream().map(CommentDto::consCommentDto).collect(Collectors.toList());

        return ItemWithBookingDto.consBookingForItemDto(item, lastBooking, nextBooking, commentDtos);
    }

    @Mapping(source = "isAvailable", target = "available")
    public abstract ItemDto toDto(Item item);

    @Mapping(source = "available", target = "isAvailable")
    public abstract Item toItem(ItemDto itemDto);

    public Item toItem(ItemDto itemDto, User user) {
        return enrichWithUser(toItem(itemDto), user);
    }

    private Item enrichWithUser(Item item, User user) {
        item.setOwner(user);
        return item;
    }
}
