package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingForItemResponseEntityDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@SpringBootTest
class BookingMapperTest {

    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;
    private Item item;
    private User user;
    private UserDTO userDTO;
    private Booking booking;
    private BookingDTO bookingDTO;

    @BeforeEach
    public void defaultSetUp() {

        user = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userDTO = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        item = Item.builder()
                .id(1)
                .name("Пила")
                .owner(user)
                .available(true)
                .build();
        booking = Booking.builder()
                .id(1)
                .booker(user)
                .item(item)
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().minusHours(3))
                .build();
        bookingDTO = BookingDTO.builder()
                .itemId(1)
                .booker(userDTO)
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().minusHours(3))
                .build();
    }

    @Test
    void bookingToBookingResponseEntityDto() {
        BookingResponseEntityDTO bookingResponseEntityDTO = bookingMapper.bookingToBookingResponseEntityDto(booking);
        Assertions.assertNotNull(bookingResponseEntityDTO);
        Assertions.assertEquals(booking.getId(), bookingResponseEntityDTO.getId());
    }

    @Test
    void bookingToBookingResponseForItemEntityDto() {
        BookingForItemResponseEntityDTO bookingForItemResponseEntityDTO = bookingMapper.bookingToBookingResponseForItemEntityDto(booking);
        Assertions.assertNotNull(bookingForItemResponseEntityDTO);
        Assertions.assertEquals(booking.getId(), bookingForItemResponseEntityDTO.getId());
    }

    @Test
    void bookingDtoToBooking() {
        Booking booking1 = bookingMapper.bookingDtoToBooking(bookingDTO);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(bookingDTO.getId(), booking1.getId());
    }

    @Test
    void createBookingForItemResponseEntityDTO() {
        BookingForItemResponseEntityDTO bookingForItemResponseEntityDTO = BookingForItemResponseEntityDTO.builder()
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(7))
                .bookerId(1)
                .build();
        Assertions.assertNotNull(bookingForItemResponseEntityDTO);
        Assertions.assertNotNull(bookingForItemResponseEntityDTO.getBookerId());
        Assertions.assertNotNull(bookingForItemResponseEntityDTO.getStart());
        Assertions.assertNotNull(bookingForItemResponseEntityDTO.getEnd());
        Assertions.assertNotNull(bookingForItemResponseEntityDTO.getStatus());
    }
}