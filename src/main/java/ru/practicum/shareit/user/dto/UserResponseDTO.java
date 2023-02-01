package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
}
