package ru.practicum.shareit.booking.enums;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.exceptions.NoSuchStateException;

import java.util.Optional;

public class BookingStateConverter implements Converter<String, BookingState> {
    @Override
    public BookingState convert(String source) {
        Optional<BookingState> state = getStatus(source.toUpperCase());
        return state.orElseThrow(() -> new NoSuchStateException("Unknown state: " + source));
    }

    private Optional<BookingState> getStatus(String source) {
        for (BookingState value : BookingState.values()) {
            if (value.getName().equals(source)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}