package ru.practicum.shareit.user.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Entity
@Table(name = "users", schema = "public", uniqueConstraints = { @UniqueConstraint(name = "UniqueEmail",
        columnNames = { "email" }) })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "имя не должно быть пустым")
    private String name;
    @Email(message = "некорректный формат электронной почты")
    @NotBlank(message = "электронная почта не должна быть пустой")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
