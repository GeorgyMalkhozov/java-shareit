package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class ItemRequestMapperTest {

    @Autowired
    private ItemRequestMapper requestMapper;
    private ItemRequestDTO requestDTO;

    @Autowired
    private UserMapper userMapper;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void defaultSetUp() {
        requestDTO = ItemRequestDTO.builder()
                .description("Требуется пила")
                .build();
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
    }

    @Test
    void itemRequestToResponseDto() {
        ItemRequest request = requestMapper.dtoToItemRequest(requestDTO);
        request.setRequestor(user);
        ItemRequestResponseDTO responseDTO = requestMapper.itemRequestToResponseDto(request);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(requestDTO.getDescription(), responseDTO.getDescription());
    }

    @Test
    void dtoToItemRequest() {
        ItemRequest request = requestMapper.dtoToItemRequest(requestDTO);
        Assertions.assertNotNull(request);
        Assertions.assertEquals(requestDTO.getDescription(), request.getDescription());
    }
}