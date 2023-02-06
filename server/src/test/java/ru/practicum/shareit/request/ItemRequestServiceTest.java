package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
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

class ItemRequestServiceTest {

    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;
    private ItemService itemService;
    private UserService userService;
    private ItemRequestMapper requestMapper;
    private User owner;
    private UserDTO ownerDTO;
    private UserResponseDTO ownerResponseDTO;
    private User requestor;
    private UserDTO requestorDTO;
    private Item item;
    private ItemDTO itemDTO;
    private ItemResponseDTO itemResponseDTO;
    private ItemRequest itemRequest;
    private ItemRequestDTO itemRequestDTO;
    private ItemRequestResponseDTO itemRequestResponseDTO;

    @BeforeEach
    public void defaultSetUp() {

        itemService = mock(ItemService.class);
        userService = mock(UserService.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        requestMapper = mock(ItemRequestMapper.class);
        itemRequestService = new ItemRequestService(itemRequestRepository, itemService,
                userService, requestMapper);

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
                .id(2)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        requestor = User.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();
        requestorDTO = UserDTO.builder()
                .id(2)
                .name("Олег")
                .email("oleg@mail.ru")
                .build();

        item = Item.builder()
                .id(1)
                .name("Пила")
                .owner(owner)
                .available(true)
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
        itemRequest = ItemRequest.builder()
                .id(1)
                .description("Требуется пила")
                .created(LocalDateTime.now().minusHours(4))
                .requestor(requestor)
                .build();
        itemRequestDTO = ItemRequestDTO.builder()
                .description("Требуется пила")
                .build();
        itemRequestResponseDTO = ItemRequestResponseDTO.builder()
                .id(1)
                .description("Требуется пила")
                .created(LocalDateTime.now().minusHours(4))
                .requestor(requestorDTO)
                .build();

        when(requestMapper.dtoToItemRequest(any(ItemRequestDTO.class))).thenReturn(itemRequest);
        when(requestMapper.itemRequestToResponseDto(any(ItemRequest.class))).thenReturn(itemRequestResponseDTO);
        doNothing().when(userService).checkUserIdExist(anyInt());
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.getById(anyInt())).thenReturn(itemRequest);
    }

    @Test
    void addItemRequest() {
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        Assertions.assertEquals(itemRequestResponseDTO,
                itemRequestService.addItemRequest(itemRequestDTO, requestor.getId()));
    }

    @Test
    void getItemRequest() {
        when(itemRequestRepository.getById(anyInt()))
                .thenReturn(itemRequest);
        Assertions.assertEquals(itemRequestResponseDTO,
                itemRequestService.getItemRequest(itemRequest.getId(), requestor.getId()));
    }

    @Test
    void findAllItemRequestsByCurrentUser() {
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of(itemRequest));
        Assertions.assertEquals(List.of(itemRequestResponseDTO),
                itemRequestService.findAllItemRequestsByCurrentUser(requestor.getId()));
    }

    @Test
    void findOthersItemRequests() {
        when(itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Assertions.assertEquals(List.of(itemRequestResponseDTO),
                itemRequestService.findOthersItemRequests(owner.getId(), 0, 5));
    }

    @Test
    void checkRequestIdExist() {
        itemRequestService.checkRequestIdExist(1);
        verify(itemRequestRepository, atLeast(1)).findById(1);
    }
}