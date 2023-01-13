package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponseDTO addUser(UserDTO dto) {
        User user = userRepository.save(userMapper.userDtoToUser(dto));
        return userMapper.userToUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUser(Integer id) {
        return userMapper.userToUserResponseDto(getUserById(id));
    }

    @Transactional
    public UserResponseDTO putUser(UserDTO dto) {
        checkUserExist(dto.getId());
        User user = userRepository.save(userMapper.userDtoToUser(dto));
        return userMapper.userToUserResponseDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Integer id) {
        checkUserExist(id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {
        User user = getUserById(id);
        userMapper.updateUserFromDto(dto, user);
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        checkUserExist(id);
        return userRepository.getById(id);
    }

    public void checkUserExist(Integer userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UnknownIdException("Пользователь с id = " + userId + " не существует");
        }
    }
}
