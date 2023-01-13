package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseEntityDTO;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStateConverter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dao.ItemCheckDao;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final ItemCheckDao itemCheckDao;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ItemRepository itemRepository, UserService userService,
                          BookingMapper bookingMapper, ItemCheckDao itemCheckDao) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingMapper = bookingMapper;
        this.itemCheckDao = itemCheckDao;
    }

    @Transactional
    public BookingResponseEntityDTO addBookingRequest(BookingDTO bookingDto, Integer userId) {
        preAddCheckForBooking(bookingDto, userId);
        Booking booking = bookingMapper.bookingDtoToBooking(bookingDto);
        enrichBookingWithData(booking, userId, bookingDto.getItemId());
        bookingRepository.save(booking);
        return bookingMapper.bookingToBookingResponseEntityDto(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponseEntityDTO getBookingRequest(Integer bookingId, Integer userId) {
        checkBookingExist(bookingId);
        checkUserAccessToBookingRequest(userId, bookingId);
        return bookingMapper.bookingToBookingResponseEntityDto(bookingRepository.getById(bookingId));
    }

    @Transactional(readOnly = true)
    public List<BookingResponseEntityDTO> findAllBookingsByCurrentUser(Integer userId, String bookingState) {
        userService.checkUserExist(userId);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        return filterForResponse(bookings, bookingState).stream()
                .map(bookingMapper::bookingToBookingResponseEntityDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseEntityDTO> findAllBookingsForOwner(Integer userId, String bookingState) {
        userService.checkUserExist(userId);
        checkOwnerHasItemsToBook(userId);
        List<Booking> bookings = bookingRepository.findAllBookingsForOwner(userId);
        return filterForResponse(bookings, bookingState).stream()
                .map(bookingMapper::bookingToBookingResponseEntityDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseEntityDTO approveBooking(Integer bookingId, Integer userId, boolean approved) {
        preApproveBookingCheck(bookingId, userId);
        Booking booking = bookingRepository.getById(bookingId);
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapper.bookingToBookingResponseEntityDto(booking);
    }

    public void checkUserAccessToBookingRequest(Integer userId, Integer bookingId) {
        if (!Objects.equals(bookingRepository.getById(bookingId).getBooker().getId(), userId) &&
                !Objects.equals(bookingRepository.getById(bookingId).getItem().getOwner().getId(), userId)) {
            throw new NoObjectsFoundException("Только владелец вещи или автор бронирования " +
                    "может просматривать бронь");
        }
    }

    private void enrichBookingWithData(Booking booking, Integer userId, Integer itemId) {
       addBookerToBooking(booking, userId);
       addItemToBookingEntity(booking, itemId);
       booking.setStatus(BookingStatus.WAITING);
    }

    private void addBookerToBooking(Booking booking, Integer userId) {
        booking.setBooker(userService.getUserById(userId));
    }

    private void addItemToBookingEntity(Booking booking, Integer itemId) {
        booking.setItem(itemRepository.getById(itemId));
    }

    private List<Booking> filterForResponse(List<Booking> bookings, String state) {
        BookingState bookingState = Optional.ofNullable(new BookingStateConverter().convert(state))
                        .orElseThrow(() -> new NoSuchStateException("Unknown state: " + state));
        switch (bookingState) {
            case REJECTED:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookings.stream()
                    .filter(booking -> (booking.getStart().isBefore(LocalDateTime.now()) &&
                            booking.getEnd().isAfter(LocalDateTime.now())))
                    .collect(Collectors.toList());
            default:
                return bookings;
        }
    }

    private void preAddCheckForBooking(BookingDTO bookingDto, Integer userId) {
        checkEndDateIsAfterStartDateExist(bookingDto);
        userService.checkUserExist(userId);
        itemCheckDao.checkItemExist(bookingDto.getItemId());
        itemCheckDao.checkItemAvailability(bookingDto.getItemId());
        checkOwnerIsNotBooker(bookingDto.getItemId(), userId);
    }

    private void preApproveBookingCheck(Integer bookingId, Integer userId) {
        checkBookingExist(bookingId);
        userService.checkUserExist(userId);
        itemCheckDao.checkUserAccessToItem(userId, bookingRepository.getById(bookingId).getItem());
        checkCorrectStatusBeforeBookingApproval(bookingId);
    }

    private void checkBookingExist(Integer bookingId) {
        if (!bookingRepository.findById(bookingId).isPresent()) {
            throw new UnknownIdException("Брони с id = " + bookingId + " не существует");
        }
    }

    private void checkEndDateIsAfterStartDateExist(BookingDTO dto) {
        if (dto.getEnd().isBefore(dto.getStart())) {
            throw new NegativeBookingLengthException("Дата начала аренды должна быть раньше даты конца аренды");
        }
    }

    private void checkOwnerIsNotBooker(Integer itemId, Integer userId) {
        if (itemRepository.getById(itemId).getOwner().getId().equals(userId)) {
            throw new UnknownIdException("Владелец не может арендовать свою вещь");
        }
    }

    private void checkOwnerHasItemsToBook(Integer userId) {
        if (itemRepository.findAllByOwnerId(userId).isEmpty()) {
            throw new UnknownIdException("У пользователя нет вещей в аренде");
        }
    }

    private void checkCorrectStatusBeforeBookingApproval(Integer bookingId) {
        if (!bookingRepository.getById(bookingId).getStatus().equals(BookingStatus.WAITING)) {
            throw new WrongApproveException("Текущий статус бронирования не предполагает одобрения " +
                    "или отказа в аренде (должен быть статус WAITING)");
        }
    }

    public void addLastAndNextBookingsToItem(ItemResponseDTO dto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItemIdAndEndIsBeforeOrderByEndDesc(dto.getId(), currentTime);
        lastBooking.ifPresent(booking -> dto.setLastBooking(bookingMapper
                .bookingToBookingResponseForItemEntityDto(booking)));
        Optional<Booking> nextBooking = bookingRepository
                .findFirstByItemIdAndStartIsAfterOrderByStartAsc(dto.getId(), currentTime);
        nextBooking.ifPresent(booking -> dto.setNextBooking(bookingMapper
                .bookingToBookingResponseForItemEntityDto(booking)));
    }

    public void checkCommentatorWasABookerBeforeComment(Integer itemId, Integer userId) {
        if (bookingRepository.findBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(userId, itemId,
                LocalDateTime.now(), BookingStatus.APPROVED).isEmpty()) {
            throw new NoCommentWithoutBookingException(
                    "Оставлять комментарии может только пользователь бравший ее в аренду");
        }
    }
}
