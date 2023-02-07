package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.CustomExceptionHandler;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserResponseDTO userDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(CustomExceptionHandler.class)
                .build();

        userDto = new UserResponseDTO(
                1,
                "John Doe",
                "john.doe@mail.com");
    }

    @Test
    void addUser() throws Exception {
            when(userService.addUser(any()))
                    .thenReturn(userDto);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                    .andExpect(jsonPath("$.name", is(userDto.getName())))
                    .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void addUserWithIllegalArgument() throws Exception {
        when(userService.addUser(any()))
                .thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }

    @Test
    void addDuplicateUser() throws Exception {
        when(userService.addUser(any()))
                .thenThrow(DuplicateDataException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(409));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(any()))
                .thenReturn(userDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getUserNoUserExist() throws Exception {
        when(userService.getUser(any()))
                .thenThrow(UnknownIdException.class);

        mvc.perform(get("/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void putUser() throws Exception {
        UserResponseDTO renewedUserDto = new UserResponseDTO(
                1,
                "John Doe 1",
                "john.doe@mail.com");
        when(userService.putUser(any()))
                .thenReturn(renewedUserDto);

        mvc.perform(put("/users")
                        .content(mapper.writeValueAsString(renewedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(renewedUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(renewedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(renewedUserDto.getEmail())));
    }

    @Test
    void removeUser() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

    }

    @Test
    void findAllUsers() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(List.of(userDto));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        UserResponseDTO renewedUserDto = new UserResponseDTO(
                1,
                "John Doe 2",
                "john.doe@mail.com");
        when(userService.updateUser(any(), any()))
                .thenReturn(renewedUserDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(renewedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(renewedUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(renewedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(renewedUserDto.getEmail())));
    }
}