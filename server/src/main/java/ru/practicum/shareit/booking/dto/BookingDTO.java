package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer itemId;
    private UserDTO booker;

}
