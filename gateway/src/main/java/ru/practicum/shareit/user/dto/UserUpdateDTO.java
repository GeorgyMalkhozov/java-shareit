package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserUpdateDTO {

    private Integer id;
    private String name;
    @Email(message = "электронная почта не соответствует формату")
    private String email;
}
