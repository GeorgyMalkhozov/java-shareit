package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Data
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
