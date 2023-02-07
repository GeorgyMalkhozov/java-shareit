package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Validated
public class ItemRequestResponseDTO {

    private Integer id;
    private String description;
    private UserDTO requestor;
    private LocalDateTime created;
    private List<ItemResponseDTO> items;
}
