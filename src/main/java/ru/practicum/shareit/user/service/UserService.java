package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO addUser(UserDTO dto) {
        User user = userMapper.userDtoToUser(dto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("Email должен быть уникальными");
        }
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDTO getUser(Integer id) {
        checkUserIdExist(id);
        return userMapper.userToUserResponseDto(getUserById(id));
    }

    public UserResponseDTO putUser(UserDTO dto) {
        checkUserIdExist(dto.getId());
        User user = userRepository.save(userMapper.userDtoToUser(dto));
        return userMapper.userToUserResponseDto(userRepository.save(user));
    }

    public void deleteUser(Integer id) {
        checkUserIdExist(id);
        userRepository.deleteById(id);
    }

    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {
        checkUserIdExist(id);
        User user = getUserById(id);
        userMapper.updateUserFromDto(dto, user);
        try {
            userRepository.save(user);
        } catch (TransactionSystemException e) {
            throw new ValidationException("поля name и email не могут быть пустыми");
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("Email должен быть уникальным");
        }
        return userMapper.userToUserResponseDto(user);
    }

    public User getUserById(Integer id) {
        checkUserIdExist(id);
        return userRepository.getById(id);
    }

    public void checkUserIdExist(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UnknownIdException("Пользователь с id = " + userId + " не существует");
        }
    }
}
