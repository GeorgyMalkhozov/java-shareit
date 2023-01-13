package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
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
    public List<BookingResponseEntityDTO> findAllBookingsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                                       @RequestParam(name = "state", defaultValue = "ALL")
                                                                 String state) {
        return bookingService.findAllBookingsByCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseEntityDTO> findAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                                  @RequestParam(name = "state", defaultValue = "ALL")
                                                                 String state) {
        return bookingService.findAllBookingsForOwner(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseEntityDTO approveBooking(@PathVariable Integer bookingId,
                                                   @RequestParam(name = "approved") boolean approved,
                                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
