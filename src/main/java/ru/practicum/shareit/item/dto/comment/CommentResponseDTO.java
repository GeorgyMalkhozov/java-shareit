package ru.practicum.shareit.item.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
public class CommentResponseDTO {

    private Integer id;
    private String text;
    private ItemDTO item;
    private String authorName;
    private LocalDateTime created;
}
