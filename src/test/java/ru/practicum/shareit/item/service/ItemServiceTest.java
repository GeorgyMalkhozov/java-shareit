package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NoObjectsFoundException;
import ru.practicum.shareit.item.dao.ItemCheckDao;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    private ItemService itemService;
    private ItemMapper itemMapper;
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private BookingService bookingService;
    private CommentMapper commentMapper;
    private ItemCheckDao itemCheckDao;
    private ItemDTO itemDTO;
    private Item item;
    private UserDTO ownerDto;
    private User owner;
    private UserMapper userMapper;
    private ItemResponseDTO itemResponseDTO;
    private UserResponseDTO ownerResponseDTO;
    private Comment comment;
    private CommentDTO commentDTO;
    private CommentResponseDTO commentResponseDTO;
    private User commentator;
    private UserResponseDTO commentatorDTO;

    @BeforeEach
    public void defaultSetUp() {
        itemRepository = mock(ItemRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        userService = mock(UserService.class);
        bookingService = mock(BookingService.class);
        commentMapper = mock(CommentMapper.class);
        userMapper = mock(UserMapper.class);
        itemCheckDao = mock(ItemCheckDao.class);
        itemMapper = mock(ItemMapper.class);
        itemService = new ItemService(itemRepository, commentRepository, itemRequestRepository, userService,
                itemMapper, bookingService, commentMapper, itemCheckDao);
        owner = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        ownerDto = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        ownerResponseDTO = UserResponseDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        itemDTO = ItemDTO.builder()
                .id(1)
                .name("Пила")
                .owner(ownerDto)
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
        commentator = User.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();
        commentatorDTO = UserResponseDTO.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();
        comment = Comment.builder()
                .item(item)
                .id(1)
                .text("Комментарий")
                .author(commentator)
                .created(LocalDateTime.now())
                .build();
        commentDTO = CommentDTO.builder()
                .text("Комментарий")
                .build();
        commentResponseDTO = CommentResponseDTO.builder()
                .item(itemDTO)
                .id(1)
                .authorName(commentator.getName())
                .text("Комментарий")
                .created(LocalDateTime.now())
                .build();
        when(itemMapper.itemDtoToItem(any(ItemDTO.class)))
                .thenReturn(item);
        when(itemMapper.itemToItemResponseDto(any(Item.class)))
                .thenReturn(itemResponseDTO);
        when(commentMapper.commentDtoToComment(any(CommentDTO.class)))
                .thenReturn(comment);
        when(commentMapper.commentToCommentForItemResponseEntityDto(any(Comment.class)))
                .thenReturn(commentResponseDTO);
        doNothing().when(userService).checkUserIdExist(anyInt());
    }

    @Test
    void addItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        Assertions.assertEquals(item.getName(), itemService.addItem(itemDTO, owner.getId()).getName());
    }

    @Test
    void getItem() {
        when(itemRepository.getById(anyInt()))
                .thenReturn(item);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        Assertions.assertEquals(item.getName(), itemService.getItem(item.getId(), owner.getId()).getName());
    }

    @Test
    void updateItem() {
        ItemUpdateDTO itemUpdateDTO = ItemUpdateDTO.builder()
                .name("Дрель")
                .build();
        itemResponseDTO = ItemResponseDTO.builder()
                .id(item.getId())
                .name(itemUpdateDTO.getName())
                .owner(ownerResponseDTO)
                .available(true)
                .build();
        when(itemRepository.getById(anyInt())).thenReturn(item);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        item.setName(itemUpdateDTO.getName());
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(itemMapper.itemToItemResponseDto(any(Item.class)))
                .thenReturn(itemResponseDTO);
        Assertions.assertEquals(item.getName(),
                itemService.updateItem(item.getId(), itemUpdateDTO, owner.getId()).getName());
    }

    @Test
    void searchItems() {
        when(itemRepository.searchItemsContainsText(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        Assertions.assertEquals(List.of(itemResponseDTO), itemService.searchItems("пила",0,5));
    }

    @Test
    void searchItemsNoText() {
        when(itemRepository.searchItemsContainsText(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        Assertions.assertEquals(List.of(), itemService.searchItems("",0,5));
    }

    @Test
    void searchItemsNoMatch() {
        when(itemRepository.searchItemsContainsText(anyString(), any(Pageable.class)))
                .thenThrow(NoObjectsFoundException.class);
        Assertions.assertEquals(List.of(), itemService.searchItems("",0,5));
        Assertions.assertThrows(NoObjectsFoundException.class,
                () -> itemService.searchItems("Велосипед",0,5));
    }

    @Test
    void putItem() {
        itemDTO = ItemDTO.builder()
                .id(1)
                .name("Пила")
                .owner(ownerDto)
                .available(true)
                .build();
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(any()))
                .thenReturn(item);
        Assertions.assertEquals(itemResponseDTO, itemService.putItem(itemDTO, 1));
    }

    @Test
    void deleteItem() {
        doNothing().when(itemRepository).deleteById(anyInt());
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(anyInt());
        doNothing().when(itemCheckDao).checkItemExist(anyInt());
        doNothing().when(itemCheckDao).checkUserAccessToItem(anyInt(), any(Item.class));
        itemService.deleteItem(1, 1);
        verify(itemRepository, atLeast(1)).deleteById(1);
    }

    @Test
    void findAllItems() {
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        Assertions.assertEquals(List.of(itemResponseDTO), itemService.findAllItems(1,0,5));
    }

    @Test
    void addComment() {
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        doNothing().when(itemCheckDao).checkItemExist(anyInt());
        when(itemRepository.getById(anyInt())).thenReturn(item);
        Assertions.assertEquals(commentResponseDTO.getText(),
                itemService.addComment(commentDTO, 1, 2).getText());
    }

    @Test
    void getItemsToItemRequest() {
        when(itemRepository.findAllByRequestIdOrderByIdAsc(anyInt())).thenReturn(List.of(item));
        Assertions.assertEquals(List.of(itemResponseDTO),
                itemService.getItemsToItemRequest(1));
    }
}