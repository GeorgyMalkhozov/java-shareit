package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDTO dto) {
        return userClient.addUser(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Integer id) {
        return userClient.getUser(id);
    }

   /* @PutMapping
    public UserResponseDTO putUser(@Valid @RequestBody UserDTO dto) {
        return userClient.putUser(dto);
    }*/

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        userClient.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        return userClient.findAllUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateDTO updateDto) {
        return userClient.updateUser(id, updateDto);
    }
}
