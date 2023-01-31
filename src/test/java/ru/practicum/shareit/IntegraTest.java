package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.exceptions.NoCommentWithoutBookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest
class IntegraTest {

    @Autowired
    private ItemController itemController;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserController userController;
    @Autowired
    private BookingController bookingController;
    @Autowired
    private ItemRequestController requestController;

    private UserDTO ownerDTO;
    private UserDTO otherUserDTO;
    private ItemDTO itemDTO;
    private BookingDTO bookingDTO;
    private CommentDTO commentDTO;

    @BeforeEach
    public void defaultSetUp() {
        ownerDTO = UserDTO.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        otherUserDTO = UserDTO.builder()
                .name("Иван")
                .email("ivan@mail.ru")
                .build();
        itemDTO = ItemDTO.builder()
                .name("Пила")
                .description("Ручная пила")
                .available(true)
                .build();
        bookingDTO = BookingDTO.builder()
                .start(LocalDateTime.now().minusHours(7))
                .end(LocalDateTime.now().minusHours(5))
                .build();
        commentDTO = CommentDTO.builder()
                .text("Комментарий")
                .build();
    }

    @Test
    void addUser() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        Assertions.assertEquals(ownerDTO.getName(), ownerResponseDTO.getName());
    }

    @Test
    void getUser() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        Assertions.assertEquals(ownerDTO.getName(),
                userController.getUser(ownerResponseDTO.getId()).getName());
    }

    @Test
    void updateUser() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .name("Виктор")
                .build();
        Assertions.assertEquals(userUpdateDTO.getName(),
                userController.updateUser(ownerResponseDTO.getId(), userUpdateDTO).getName());
    }

    @Test
    void addItem() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        Assertions.assertEquals(itemDTO.getName(), itemController.addItem(itemDTO, ownerResponseDTO.getId()).getName());
    }

    @Test
    void addItemWrongUserId() {
        Assertions.assertThrows(UnknownIdException.class, () -> itemController.addItem(itemDTO, 2));
    }

    @Test
    void getItem() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        ItemResponseDTO itemResponseDTO = itemController.addItem(itemDTO, ownerResponseDTO.getId());
        Assertions.assertEquals(itemDTO.getName(),
                itemController.getItem(itemResponseDTO.getId(), ownerResponseDTO.getId()).getName());
    }

    @Test
    void addCommentWithoutBooking() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        ItemResponseDTO itemResponseDTO = itemController.addItem(itemDTO, ownerResponseDTO.getId());
        Assertions.assertThrows(NoCommentWithoutBookingException.class, () -> itemController.addComment(commentDTO,
                itemResponseDTO.getId(), ownerResponseDTO.getId()));
    }

    @Test
    void addCommentByItemOwner() {
        UserResponseDTO ownerResponseDTO = userController.addUser(ownerDTO);
        ItemResponseDTO itemResponseDTO = itemController.addItem(itemDTO, ownerResponseDTO.getId());
        BookingDTO bookingDTO1 = BookingDTO.builder()
                .itemId(itemResponseDTO.getId())
                .booker(ownerDTO)
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(6))
                .build();
        Assertions.assertThrows(UnknownIdException.class, () -> bookingController
                .addBookingRequest(bookingDTO1, ownerResponseDTO.getId()));
    }
}