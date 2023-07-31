package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    public BookingController(BookingService bookingService, ItemService itemService, UserService userService) {
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public BookingOutputDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @RequestBody BookingInputDto bookingDto) {
        Booking booking = MAPPER.toBooking(
                bookingDto,
                itemService.getItem(bookingDto.getItemId()),
                userService.getUser(userId));
        return MAPPER.toDto(bookingService.createBooking(booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto updateBookingStatusByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @PathVariable Integer bookingId,
                                                       @RequestParam("approved") Optional<Boolean> isApproved) {
        Booking booking = bookingService.updateBookingStatusByOwner(userId, bookingId, isApproved);
        return MAPPER.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @PathVariable Integer bookingId) {
        Booking booking = bookingService.getBookingById(userId, bookingId);
        return MAPPER.toDto(booking);
    }

    @GetMapping
    public List<BookingOutputDto> getBookingsWithState(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam Optional<String> state) {
        List<Booking> bookings = bookingService.getBookingsWithState(userId, BookingState.fromString(state));
        return bookings.stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getBookingsWithStateForOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                               @RequestParam Optional<String> state) {
        List<Booking> bookings = bookingService.getBookingsWithStateForOwner(userId, BookingState.fromString(state));
        return bookings.stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }
}
