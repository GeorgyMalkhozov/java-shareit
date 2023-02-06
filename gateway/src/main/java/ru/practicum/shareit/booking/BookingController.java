package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBookingRequest(@RequestBody @Valid BookingDTO dto,
                                                    @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingClient.bookItem(dto, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingRequest(@PathVariable Integer bookingId,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByCurrentUser(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return bookingClient.findAllBookingsByCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "ALL")
            String state,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return bookingClient.findAllBookingsForOwner(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Integer bookingId,
                                                   @RequestParam(name = "approved") boolean approved,
                                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingClient.approveBooking(bookingId, userId, approved);
    }
}
