package ru.practicum.shareit.item.dto.comment;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CommentDTO {

    private String text;
}
