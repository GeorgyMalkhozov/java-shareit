package ru.practicum.shareit.booking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingState {

    ALL("ALL", 1),
    CURRENT("CURRENT", 2),
    PAST("PAST", 3),
    FUTURE("FUTURE", 4),
    WAITING("WAITING", 5),
    REJECTED("REJECTED", 6);

    private final String name;
    private final int id;
}
