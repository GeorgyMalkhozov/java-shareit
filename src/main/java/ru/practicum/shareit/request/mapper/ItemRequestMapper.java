package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = UserMapper.class, componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDTO requestToRequestDto(ItemRequest itemRequest);

    ItemRequest requestDtoToRequest(ItemRequestDTO dto);
}
