package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDTO addUser(@RequestBody @Valid UserDTO dto) {
        return userService.addUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping
    public UserResponseDTO putUser(@Valid @RequestBody UserDTO dto) {
        return userService.putUser(dto);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserResponseDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @PatchMapping("/{id}")
    public UserResponseDTO updateItem(@PathVariable Integer id, @RequestBody @Valid UserUpdateDTO updateDto) {
        return userService.updateUser(id, updateDto);
    }
}
