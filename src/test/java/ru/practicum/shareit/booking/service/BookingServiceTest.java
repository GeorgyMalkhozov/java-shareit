package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.enums.BookingStateConverter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dao.ItemCheckDao;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserService userService;
    private BookingMapper bookingMapper;
    private ItemCheckDao itemCheckDao;
    private User owner;
    private UserDTO ownerDTO;
    private UserResponseDTO ownerResponseDTO;
    private User booker;
    private UserDTO bookerDTO;
    private Item item;
    private ItemDTO itemDTO;
    private ItemResponseDTO itemResponseDTO;
    private Booking booking1;
    private BookingDTO booking1DTO;
    private BookingResponseEntityDTO booking1ResponseEntityDTO;

    private BookingStateConverter bookingStateConverter;

    @BeforeEach
    public void defaultSetUp() {

        itemRepository = mock(ItemRepository.class);
        userService = mock(UserService.class);
        bookingRepository = mock(BookingRepository.class);
        bookingMapper = mock(BookingMapper.class);
        itemCheckDao = mock(ItemCheckDao.class);
        bookingStateConverter = mock(BookingStateConverter.class);
        bookingService = new BookingService(bookingRepository, itemRepository,
                userService, bookingMapper, itemCheckDao);

        owner = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        ownerDTO = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        ownerResponseDTO = UserResponseDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        booker = User.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();
        bookerDTO = UserDTO.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();
        itemDTO = ItemDTO.builder()
                .id(1)
                .name("Пила")
                .owner(ownerDTO)
                .available(true)
                .build();
        itemResponseDTO = ItemResponseDTO.builder()
                .id(1)
                .name("Пила")
                .owner(ownerResponseDTO)
                .available(true)
                .build();
        item = Item.builder()
                .id(1)
                .name("Пила")
                .owner(owner)
                .available(true)
                .build();
        booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusHours(7))
                .end(LocalDateTime.now().minusHours(5))
                .status(BookingStatus.APPROVED)
                .item(item)
                .booker(booker)
                .build();
        booking1DTO = BookingDTO.builder()
                .id(1)
                .start(LocalDateTime.now().minusHours(7))
                .end(LocalDateTime.now().minusHours(5))
                .itemId(item.getId())
                .build();
        booking1ResponseEntityDTO = BookingResponseEntityDTO.builder()
                .id(1)
                .start(LocalDateTime.now().minusHours(7))
                .end(LocalDateTime.now().minusHours(5))
                .status(BookingStatus.APPROVED)
                .item(itemDTO)
                .build();
        when(bookingMapper.bookingDtoToBooking(any(BookingDTO.class))).thenReturn(booking1);
        when(bookingMapper.bookingToBookingResponseEntityDto(any(Booking.class))).thenReturn(booking1ResponseEntityDTO);
        doNothing().when(userService).checkUserIdExist(anyInt());
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(itemRepository.getById(anyInt())).thenReturn(item);
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking1));
    }

    @Test
    void addBookingRequest() {
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking1);
        Assertions.assertEquals(booking1ResponseEntityDTO,
                bookingService.addBookingRequest(booking1DTO, booker.getId()));
    }

    @Test
    void addBookingRequestError() {
        BookingDTO booking2DTO = BookingDTO.builder()
                .id(2)
                .booker(bookerDTO)
                .itemId(1)
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(1))
                .build();
        when(bookingRepository.save(any(Booking.class)))
                .thenThrow(NegativeBookingLengthException.class);
        Assertions.assertThrows(NegativeBookingLengthException.class,
                () -> bookingService.addBookingRequest(booking2DTO, 2));
    }

    @Test
    void getBookingRequest() {
        when(bookingRepository.getById(anyInt()))
                .thenReturn(booking1);
        Assertions.assertEquals(booking1ResponseEntityDTO,
                bookingService.getBookingRequest(booking1.getId(), booker.getId()));
    }

    @Test
    void findAllBookingsByCurrentUser() {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        Assertions.assertEquals(List.of(booking1ResponseEntityDTO),
                bookingService.findAllBookingsByCurrentUser(2,"PAST",0,5));
    }

    @Test
    void findAllBookingsByCurrentUserWrongState() {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        Assertions.assertThrows(NoSuchStateException.class,
                () -> bookingService.findAllBookingsByCurrentUser(2,"XDFSF",0,5));
    }

    @ParameterizedTest
    @ValueSource(strings = {"FUTURE", "CURRENT", "REJECTED", "WAITING", "ALL"})
    void findAllBookingsByCurrentUserNoMatch(String state) {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        Assertions.assertEquals(List.of(),
                bookingService.findAllBookingsByCurrentUser(2,state,0,5));
    }

    @Test
    void findAllBookingsForOwner() {
        when(bookingRepository.findAllBookingsForOwner(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyInt()))
                .thenReturn(List.of(item));
        Assertions.assertEquals(List.of(booking1ResponseEntityDTO),
                bookingService.findAllBookingsForOwner(1,"PAST",0,5));
    }

    @Test
    void approveBooking() {
        booking1.setStatus(BookingStatus.WAITING);
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking1);
        when(bookingRepository.findAllBookingsForOwner(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyInt()))
                .thenReturn(List.of(item));
        when(bookingRepository.getById(anyInt()))
                .thenReturn(booking1);
        Assertions.assertEquals(booking1ResponseEntityDTO,
                bookingService.approveBooking(1,1, true));
    }

    @Test
    void approveBookingWrong() {
        booking1.setStatus(BookingStatus.CANCELED);
        when(bookingRepository.findAllBookingsForOwner(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyInt()))
                .thenReturn(List.of(item));
        when(bookingRepository.getById(anyInt()))
                .thenReturn(booking1);
        Assertions.assertThrows(WrongApproveException.class,
                () -> bookingService.approveBooking(1,1, true));
    }

    @Test
    void checkUserAccessToBookingRequestFailure() {
        when(bookingRepository.getById(anyInt())).thenThrow(NoObjectsFoundException.class);
        Assertions.assertThrows(NoObjectsFoundException.class,
                () -> bookingService.checkUserAccessToBookingRequest(1,2));
    }

    @Test
    void addLastAndNextBookingsToItem() {
        when(bookingRepository
                .findFirstByItemIdAndEndIsBeforeOrderByEndDesc(anyInt(), any()))
                .thenReturn(Optional.of(booking1));
        when(bookingRepository
                .findFirstByItemIdAndStartIsAfterOrderByStartAsc(anyInt(), any()))
                .thenReturn(Optional.empty());
        bookingService.addLastAndNextBookingsToItem(itemResponseDTO);
        verify(bookingRepository, atLeast(1))
                .findFirstByItemIdAndEndIsBeforeOrderByEndDesc(anyInt(), any());
        verify(bookingRepository, atLeast(1))
                .findFirstByItemIdAndStartIsAfterOrderByStartAsc(anyInt(), any());
    }

    @Test
    void checkCommentatorWasABookerBeforeComment() {
        when(bookingRepository.findBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(anyInt(), anyInt(),
                any(LocalDateTime.class), any(BookingStatus.class)))
                .thenThrow(NoCommentWithoutBookingException.class);
        Assertions.assertThrows(NoCommentWithoutBookingException.class,
                () -> bookingService.checkCommentatorWasABookerBeforeComment(1,2));
    }
}