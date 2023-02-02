package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemUpdateDTO {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    @Null
    private UserDTO owner;
}
