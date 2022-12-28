package ru.practicum.shareit.user.storage;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Data
@Repository
public class InMemoryUserStorage implements UserStorage {

    private static int userIdGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    public User addUser(User user) {
        checkEmail(user);
        user.setId(generateNewUserId());
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(Integer id) {
        checkUserId(id);
        return users.get(id);
    }

    public User putUser(User user) {
        checkUserId(user.getId());
        checkEmail(user);
        users.put(user.getId(), user);
        return user;
    }

    public void deleteUser(Integer id) {
        checkUserId(id);
        users.remove(id);
    }

    public List<User> findAllUsers() {
        return new ArrayList<User>(users.values());
    }

    public User updateUser(Integer id, User user) {
        if (user.getEmail() != null) {
            checkEmail(user);
        }
        User oldUser = users.get(id);
        if (user.getName() != null && !Objects.equals(oldUser.getName(), user.getName())) {
            users.get(id).setName(user.getName());
        }
        if (user.getEmail() != null && !Objects.equals(oldUser.getEmail(), user.getEmail())) {
            users.get(id).setEmail(user.getEmail());
        }
        return oldUser;
    }

    private static int generateNewUserId() {
        userIdGenerator++;
        return userIdGenerator;
    }

    public void checkUserId(Integer id) {
        if (!users.containsKey(id)) {
            throw new UnknownIdException("Указан некорректный Id пользователя");
        }
    }

    private void checkEmail(User user) {
        for (User value : users.values()) {
            if (value.getEmail().equals(user.getEmail()) && (!value.getId().equals(user.getId()))) {
                throw new DuplicateEmailException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
    }
}
