package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserMapper userMapper;
    private UserRepository userRepository;
    private UserDTO userDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void defaultSetUp() {
        userRepository = mock(UserRepository.class);
        userMapper = new UserMapperImpl();
        userService = new UserService(userRepository, userMapper);
        userDTO = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        user = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userResponseDTO = userMapper.userToUserResponseDto(user);
    }

    @Test
    void addUserSuccessful() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        Assertions.assertEquals(userMapper.userToUserResponseDto(user), userService.addUser(userDTO));
    }

    @Test
    void addUserUnsuccessfulWrongEmailFormat() {
        userDTO = UserDTO.builder()
                .name("Петр")
                .email("petrmail.ru")
                .build();
          when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);
        Assertions.assertThrows(DuplicateDataException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void addUserUnsuccessfulNoUsername() {
        userDTO = UserDTO.builder()
                .email("petr1@rmail.ru")
                .build();
        when(userRepository.save(any()))
                .thenThrow(DuplicateDataException.class);
        Assertions.assertThrows(DuplicateDataException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void getUser() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(userRepository.getById(anyInt()))
                .thenReturn(user);
        Assertions.assertEquals(userResponseDTO, userService.getUser(user.getId()));
    }

    @Test
    void putUser() {
        userDTO = UserDTO.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(user);
        Assertions.assertEquals(userResponseDTO, userService.putUser(userDTO));
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(anyInt());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        userService.deleteUser(1);
        verify(userRepository, atLeast(1)).deleteById(1);
    }

    @Test
    void findAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        Assertions.assertEquals(List.of(userResponseDTO), userService.findAllUsers());
    }

    @Test
    void updateUserSuccessful() {
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .name("Олег")
                .build();
        when(userRepository.getById(anyInt())).thenReturn(user);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        Assertions.assertEquals(userUpdateDTO.getName(),
                userService.updateUser(user.getId(), userUpdateDTO).getName());
    }

    @Test
    void updateUserWrongName() {
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .name("")
                .build();
        when(userRepository.getById(anyInt())).thenReturn(user);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userRepository.save(any(User.class)))
                .thenThrow(DuplicateDataException.class);
        Assertions.assertThrows(DuplicateDataException.class, () -> userService.updateUser(1, userUpdateDTO));

    }

    @Test
    void getUserByIdSuccess() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(userRepository.getById(anyInt()))
                .thenReturn(user);
        Assertions.assertEquals(user, userService.getUserById(user.getId()));
    }

    @Test
    void getUserByIdException() {
        when(userRepository.findById(anyInt()))
                .thenThrow(UnknownIdException.class);
        when(userRepository.getById(anyInt()))
                .thenThrow(UnknownIdException.class);
        Assertions.assertThrows(UnknownIdException.class, () -> userService.getUserById(1));
    }

    @Test
    void checkUserIdExist() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        userService.checkUserIdExist(user.getId());
        verify(userRepository, atLeast(1)).findById(1);
    }
}