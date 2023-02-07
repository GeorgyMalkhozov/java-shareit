package ru.practicum.shareit.user.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Getter
@Setter
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
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
