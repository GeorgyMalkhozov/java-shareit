package ru.practicum.shareit.item.dto.comment;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CommentDTO {

    @NotBlank
    private String text;
}
