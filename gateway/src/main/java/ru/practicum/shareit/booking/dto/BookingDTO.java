package ru.practicum.shareit.booking.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class BookingDTO {

    private Integer id;
    @Future(message = "время начала аренды должно быть в будущем")
    private LocalDateTime start;
    @Future(message = "время окончания аренды должно быть в будущем")
    private LocalDateTime end;
    private Integer itemId;
    private UserDTO booker;

}
