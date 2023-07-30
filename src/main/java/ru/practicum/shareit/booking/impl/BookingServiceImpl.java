package ru.practicum.shareit.booking.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UnknownBookingException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    public static final Sort SORT_BY_DATE_DESC = Sort.by("startTime").descending();
    private final BookingRepository bookingRepository;
    private final UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
    }

    @Override
    public Booking createBooking(Booking booking) {
        checkBookingDates(booking.getStartTime(), booking.getEndTime());
        checkIfItemAvailable(booking.getItem());
        if (booking.getItem().getOwner().getId() == booking.getBooker().getId()) {
            throw new UnknownUserException(booking.getBooker().getId());
        }
        booking.setBookingStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    private void checkBookingDates(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        if (startTime == null) {
            throw new IllegalArgumentException("Start is null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End is null");
        }
        if (endTime.isBefore(now)) {
            throw new IllegalArgumentException("Start in past");
        }
        if (startTime.isBefore(now)) {
            throw new IllegalArgumentException("End in past");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End before start");
        }
        if (endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End equals start");
        }
    }

    @Transactional
    @Override
    public Booking updateBookingStatusByOwner(Integer userId, Integer bookingId, Optional<Boolean> isApprovedO) {
        User user = userService.getUser(userId);
        boolean isApproved = isApprovedO.orElseThrow(() -> new IllegalArgumentException("Parameter \"approved\" is empty"));
        Optional<Booking> bookingO = bookingRepository.findById(bookingId);
        if (bookingO.isEmpty()) {
            throw new IllegalArgumentException("Illegal booking id: " + bookingId);
        }
        if (bookingO.get().getItem().getOwner().getId() != user.getId()) {
            throw new UnknownUserException(user.getId());
        }
        if (BookingStatus.APPROVED.equals(bookingO.get().getBookingStatus())) {
            throw new IllegalArgumentException("Booking was already approved");
        }
        bookingO.get().setBookingStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(bookingO.get());
    }

    @Override
    public Booking getBookingById(Integer userId, Integer bookingId) {
        User user = userService.getUser(userId);
        Optional<Booking> bookingO = bookingRepository.findById(bookingId);
        if (bookingO.isEmpty()) {
            throw new UnknownBookingException(bookingId);
        }
        Item item = bookingO.get().getItem();
        if (!Objects.equals(bookingO.get().getBooker().getId(), user.getId())
                && !Objects.equals(item.getOwner().getId(), user.getId())) {
            throw new UnknownUserException(user.getId());
        }
        return bookingO.get();
    }

    @Override
    public List<Booking> getBookingsWithState(Integer userId, BookingState state) {
        User user = userService.getUser(userId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST:
                return bookingRepository.findPastBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case CURRENT:
                return bookingRepository.findCurrentBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case FUTURE:
                return bookingRepository.findFutureBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case WAITING:
                return bookingRepository.findByBookerIdAndBookingStatus(user.getId(), BookingStatus.WAITING, SORT_BY_DATE_DESC);
            case REJECTED:
                return bookingRepository.findByBookerIdAndBookingStatus(user.getId(), BookingStatus.REJECTED, SORT_BY_DATE_DESC);
            case ALL:
            default:
                return bookingRepository.findAll(SORT_BY_DATE_DESC);
        }
    }

    @Override
    public List<Booking> getBookingsWithStateForOwner(Integer userId, BookingState state) {
        User user = userService.getUser(userId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST:
                return bookingRepository.findOwnerPastBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case CURRENT:
                return bookingRepository.findOwnerCurrentBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case FUTURE:
                return bookingRepository.findOwnerFutureBookingsAndSort(user.getId(), now, SORT_BY_DATE_DESC);
            case WAITING:
                return bookingRepository.findByItemOwnerIdAndBookingStatus(user.getId(), BookingStatus.WAITING, SORT_BY_DATE_DESC);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndBookingStatus(user.getId(), BookingStatus.REJECTED, SORT_BY_DATE_DESC);
            case ALL:
            default:
                return bookingRepository.findAll(SORT_BY_DATE_DESC);
        }
    }

    private void checkIfItemAvailable(Item item) {
        if (!item.getIsAvailable()) {
            throw new IllegalArgumentException("Item is unavailable, itemId=" + item.getId());
        }
    }
}
