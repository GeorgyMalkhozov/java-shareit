package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.Null;

@Data
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
