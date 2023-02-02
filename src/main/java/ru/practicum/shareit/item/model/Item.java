package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@Validated
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "название вещи не должно быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank(message = "описание вещи не должно быть пустым")
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
