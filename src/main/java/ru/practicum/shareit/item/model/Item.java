package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@Validated
public class Item {

    @Id
    private Integer id;
    @NotBlank(message = "название вещи не должно быть пустым")
    private String name;
    @NotBlank(message = "описание вещи не должно быть пустым")
    private String description;
    @NotBlank(message = "доступность вещи не должна быть пустой")
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
