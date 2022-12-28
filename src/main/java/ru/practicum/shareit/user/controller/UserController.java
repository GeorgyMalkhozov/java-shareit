package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final ItemMapper itemMapper;

    @Autowired
    public UserController(UserService userService, ItemMapper itemMapper) {
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public UserDTO addUser(@RequestBody @Valid UserDTO dto) {
        return userService.addUser(dto);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping
    public UserDTO putUser(@Valid @RequestBody User user) {
        return userService.putUser(user);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @PatchMapping("/{id}")
    public UserDTO updateItem(@PathVariable Integer id, @RequestBody @Valid UserUpdateDTO updateDto) {
        return userService.updateUser(id, updateDto);
    }
}
