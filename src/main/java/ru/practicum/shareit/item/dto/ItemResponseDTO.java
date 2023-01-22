package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingForItemResponseEntityDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Validated
public class ItemResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private UserDTO owner;
    private List<CommentResponseDTO> comments;
    private BookingForItemResponseEntityDTO lastBooking;
    private BookingForItemResponseEntityDTO nextBooking;
    private Integer requestId;
}
