package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
@Validated
public class User {
    @Id
    private Integer id;
    private String name;
    @Email(message = "некорректный формат электронной почты")
    @NotBlank(message = "электронная почта не должна быть пустой")
    private String email;
}
