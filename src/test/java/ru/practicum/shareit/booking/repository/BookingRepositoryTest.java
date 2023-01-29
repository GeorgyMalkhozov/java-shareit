package ru.practicum.shareit.booking.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    private Item item1;
    private User owner1;

    private User booker1;
    private User booker2;


    public void setUp() throws Exception {
        owner1 = User.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userRepository.save(owner1);

        booker1 = User.builder()
                .name("Иван")
                .email("ivan@mail.ru")
                .build();
        userRepository.save(booker1);

        booker2 = User.builder()
                .name("Сергей")
                .email("sergey@mail.ru")
                .build();
        userRepository.save(booker2);

        item1 = Item.builder()
                .name("Пила")
                .description("Ручная пила")
                .available(true)
                .owner(owner1)
                .build();
        itemRepository.save(item1);

        booking1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .booker(booker1)
                .status(BookingStatus.WAITING)
                .item(item1)
                .build();
        bookingRepository.save(booking1);

        booking2 = Booking.builder()
                .start(LocalDateTime.now().minusHours(7))
                .end(LocalDateTime.now().minusHours(5))
                .booker(booker2)
                .status(BookingStatus.APPROVED)
                .item(item1)
                .build();
        bookingRepository.save(booking2);

        booking3 = Booking.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(1))
                .booker(booker1)
                .status(BookingStatus.REJECTED)
                .item(item1)
                .build();
        bookingRepository.save(booking3);
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assert.assertNotNull(entityManager);
        Assert.assertNotNull(itemRepository);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(bookingRepository);
    }

    @Test
    void saveBooking() {
        booking1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);
        Assert.assertNotNull(booking1.getId());
        Assert.assertEquals(booking1.getStart(), bookingRepository.getById(booking1.getId()).getStart());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() throws Exception {
        setUp();
        Assert.assertEquals(List.of(booking1, booking3),
                bookingRepository.findAllByBookerIdOrderByStartDesc(booker1.getId(),
                        PageRequest.of(0,5)).toList());
    }

    @Test
    void findAllBookingsForOwner() throws Exception {
        setUp();
        Assert.assertEquals(List.of(booking1, booking3, booking2),
                bookingRepository.findAllBookingsForOwner(owner1.getId(),
                        PageRequest.of(0,5)).toList());
    }

    @Test
    void findBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusIs() throws Exception {
        setUp();
        Assert.assertEquals(List.of(booking2),
                bookingRepository.findBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(booker2.getId(), item1.getId(),
                        LocalDateTime.now(), BookingStatus.APPROVED));
    }

    @Test
    void findFirstByItemIdAndEndIsBeforeOrderByEndDesc() throws Exception {
        setUp();
        Assert.assertEquals(booking2,
                bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc(item1.getId(),
                        LocalDateTime.now()).get());
    }

    @Test
    void findFirstByItemIdAndStartIsAfterOrderByStartAsc() throws Exception {
        setUp();
        Assert.assertEquals(booking2,
                bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(item1.getId(),
                        LocalDateTime.now().minusHours(10)).get());
    }
}