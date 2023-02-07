package ru.practicum.shareit.request.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemRequestDTO {

    @NotBlank(message = "описание не должно быть пустым")
    private String description;
}
