package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    private Item item;
    private ItemDTO itemDTO;
    private User user;
    private UserDTO userDTO;

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
        itemDTO = ItemDTO.builder()
                .id(1)
                .name("Пила")
                .available(true)
                .build();
    }

    @Test
    void itemToItemResponseDto() {
        ItemResponseDTO responseDTO = itemMapper.itemToItemResponseDto(item);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(item.getName(), responseDTO.getName());
    }

    @Test
    void itemDtoToItem() {
        Item item1 = itemMapper.itemDtoToItem(itemDTO);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(itemDTO.getDescription(), item1.getDescription());
    }

    @Test
    void updateItemFromDto() {
        ItemUpdateDTO itemUpdateDTO = ItemUpdateDTO.builder()
                .name("Велосипед")
                .description("Горный велосипед")
                .owner(userDTO)
                .available(false)
                .build();
        itemMapper.updateItemFromDto(itemUpdateDTO, item);
        Assertions.assertEquals(itemUpdateDTO.getName(), item.getName());
    }
}