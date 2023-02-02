package ru.practicum.shareit.request.controller;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.CustomExceptionHandler;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemRequestResponseDTO requestResponseDTO;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(CustomExceptionHandler.class)
                .build();

        requestResponseDTO = ItemRequestResponseDTO.builder()
                .id(1)
                .description("Требуется ручная пила")
                .build();
    }

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(any(), any()))
                .thenReturn(requestResponseDTO);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestResponseDTO))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResponseDTO.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(requestResponseDTO.getDescription())));
    }

    @Test
    void addItemRequestValidationException() throws Exception {
        when(itemRequestService.addItemRequest(any(), any()))
                .thenThrow(ValidationException.class);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestResponseDTO))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(406));
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(any(), any()))
                .thenReturn(requestResponseDTO);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResponseDTO.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(requestResponseDTO.getDescription())));
    }

    @Test
    void findAllRequestsByCurrentUser() throws Exception {
        when(itemRequestService.findAllItemRequestsByCurrentUser(any()))
                .thenReturn(List.of(requestResponseDTO));

        mvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestResponseDTO.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(requestResponseDTO.getDescription())));
    }

    @Test
    void findOthersItemRequests() throws Exception {
        when(itemRequestService.findOthersItemRequests(any(), any(), any()))
                .thenReturn(List.of(requestResponseDTO));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestResponseDTO.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(requestResponseDTO.getDescription())));
    }
}