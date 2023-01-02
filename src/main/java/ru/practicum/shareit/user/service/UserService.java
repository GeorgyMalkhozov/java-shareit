package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public UserDTO addUser(UserDTO dto) {
        User user = userStorage.addUser(userMapper.userDtoToUser(dto));
        return userMapper.userToUserDto(user);
    }

    public UserDTO getUser(Integer id) {
        return userMapper.userToUserDto(userStorage.getUser(id));
    }

    public UserDTO putUser(User user) {
        return userMapper.userToUserDto(userStorage.putUser(user));
    }

    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }

    public List<UserDTO> findAllUsers() {
        return userStorage.findAllUsers().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Integer id, UserUpdateDTO dto) {
        userStorage.checkUserId(id);
        User user = userMapper.userUpdateDtoToUser(dto);
        return userMapper.userToUserDto(userStorage.updateUser(id, user));
    }
}
