package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select B from Booking B where B.booker.id = ?1 and B.endTime <= ?2")
    List<Booking> findPastBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    @Query("select B from Booking B where B.booker.id = ?1 and B.item.id = ?2 and B.endTime <= ?3")
    List<Booking> findPastBookingsForItemAndSort(int userId, int itemId, LocalDateTime now, Sort sort);

    @Query("select B from Booking B where B.booker.id = ?1 and B.startTime <= ?2 and B.endTime >= ?2")
    List<Booking> findCurrentBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    @Query("select B from Booking B where B.booker.id = ?1 and B.startTime >= ?2")
    List<Booking> findFutureBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndBookingStatus(int bookerId, BookingStatus status, Sort sort);

    @Query("select B from Booking B where B.item.owner.id = ?1 and B.endTime <= ?2")
    List<Booking> findOwnerPastBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    @Query("select B from Booking B where B.item.owner.id = ?1 and B.startTime <= ?2 and B.endTime >= ?2")
    List<Booking> findOwnerCurrentBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    @Query("select B from Booking B where B.item.owner.id = ?1 and B.startTime >= ?2")
    List<Booking> findOwnerFutureBookingsAndSort(int userId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerIdAndBookingStatus(int ownerId, BookingStatus status, Sort sort);

    List<Booking> findByItemId(int itemId, Sort sort);

    List<Booking> findByItemIdAndBookingStatus(int itemId, BookingStatus status, Sort sort);
}
