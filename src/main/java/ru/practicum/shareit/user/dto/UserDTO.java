package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserDTO {

    private Integer id;
    private String name;
    @Email(message = "электронная почта не соответствует формату")
    @NotBlank(message = "электронная почта не должна быть пустой")
    private String email;
}
