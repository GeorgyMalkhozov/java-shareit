package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemDTO {

    private Integer id;
    @NotBlank(message = "название вещи не должно быть пустым")
    private String name;
    @NotBlank(message = "описание вещи не должно быть пустым")
    private String description;
    @NotNull
    private Boolean available;
    @Null
    private UserDTO owner;
    private Integer requestId;
}
