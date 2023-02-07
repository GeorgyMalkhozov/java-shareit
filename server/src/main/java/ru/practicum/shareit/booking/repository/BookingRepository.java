package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    @Query(value = "SELECT B " +
            "FROM Booking AS B " +
            "LEFT JOIN Item AS It ON B.item = It " +
            "WHERE It.owner.id = ?1 " +
            "ORDER BY B.start DESC ")
    Page<Booking> findAllBookingsForOwner(Integer ownerId, Pageable pageable);

    List<Booking> findBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(
            Integer bookerId, Integer itemId, LocalDateTime end, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Integer itemId, LocalDateTime end);

    Optional<Booking> findFirstByItemIdAndStartIsAfterOrderByStartAsc(Integer itemId, LocalDateTime start);
}
