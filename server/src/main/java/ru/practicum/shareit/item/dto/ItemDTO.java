package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private UserDTO owner;
    private Integer requestId;
}
