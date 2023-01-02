package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Validated
public class ItemResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private UserDTO owner;
    private ItemRequestDTO request;
}
