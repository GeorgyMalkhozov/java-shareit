package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingForItemResponseEntityDTO {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDTO item;
    private Integer bookerId;
    private BookingStatus status;
}
