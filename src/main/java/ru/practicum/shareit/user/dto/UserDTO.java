package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserDTO {

    private Integer id;
    @NotBlank(message = "имя не должно быть пустым")
    private String name;
    @Email(message = "электронная почта не соответствует формату")
    @NotBlank(message = "электронная почта не должна быть пустой")
    private String email;
}
