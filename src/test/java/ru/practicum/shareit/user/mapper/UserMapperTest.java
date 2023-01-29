package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.model.User;

class UserMapperTest {


    private UserMapperImpl userMapper;
    private UserDTO userDTO;

    @BeforeEach
    public void defaultSetUp() {
        userMapper = new UserMapperImpl();
        userDTO = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
    }

    @Test
    void userToUserDto() {
        User user = userMapper.userDtoToUser(userDTO);
        UserDTO newUserDTO = userMapper.userToUserDto(user);
        Assertions.assertNotNull(newUserDTO);
        Assertions.assertEquals(userDTO.getId(), newUserDTO.getId());
        Assertions.assertEquals(userDTO.getName(), newUserDTO.getName());
    }

    @Test
    void userToUserResponseDto() {
        User user = userMapper.userDtoToUser(userDTO);
        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDto(user);
        Assertions.assertNotNull(userResponseDTO);
        Assertions.assertEquals(userDTO.getId(), userResponseDTO.getId());
        Assertions.assertEquals(userDTO.getName(), userResponseDTO.getName());
    }

    @Test
    void userDtoToUser() {
        User user = userMapper.userDtoToUser(userDTO);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userDTO.getId(), user.getId());
        Assertions.assertEquals(userDTO.getName(), user.getName());
    }

    @Test
    void updateUserFromDto() {
        User user = userMapper.userDtoToUser(userDTO);
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .name("Сергей")
                .build();
        userMapper.updateUserFromDto(userUpdateDTO, user);
        Assertions.assertEquals(userDTO.getId(), user.getId());
        Assertions.assertEquals(userUpdateDTO.getName(), user.getName());
    }
}