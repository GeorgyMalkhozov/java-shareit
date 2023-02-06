package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
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
