package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseEntityDTO addBookingRequest(@RequestBody @Valid BookingDTO dto,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.addBookingRequest(dto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseEntityDTO getBookingRequest(@PathVariable Integer bookingId,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getBookingRequest(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseEntityDTO> findAllBookingsByCurrentUser(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return bookingService.findAllBookingsByCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseEntityDTO> findAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                                  @RequestParam(name = "state", defaultValue = "ALL")
                                                                 String state,
                                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                                  @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return bookingService.findAllBookingsForOwner(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseEntityDTO approveBooking(@PathVariable Integer bookingId,
                                                   @RequestParam(name = "approved") boolean approved,
                                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
