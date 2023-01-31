package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@Validated
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
}
