package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingForItemResponseEntityDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, ItemMapper.class}, componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "item")
    @Mapping(target = "status")
    BookingResponseEntityDTO bookingToBookingResponseEntityDto(Booking booking);

    @Mapping(target = "item")
    @Mapping(target = "bookerId", source = "booking.booker.id")
    @Mapping(target = "status")
    BookingForItemResponseEntityDTO bookingToBookingResponseForItemEntityDto(Booking booking);

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking bookingDtoToBooking(BookingDTO dto);
}
