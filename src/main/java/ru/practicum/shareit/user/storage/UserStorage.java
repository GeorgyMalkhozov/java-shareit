package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User getUser(Integer id);

    User putUser(User user);

    void deleteUser(Integer id);

    List<User> findAllUsers();

    User updateUser(Integer id, User user);

    void checkUserId(Integer id);
}
