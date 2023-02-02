package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, ItemService.class}, componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "id")
    @Mapping(target = "requestor")
    @Mapping(target = "created")
    @Mapping(target = "items", ignore = true)
    ItemRequestResponseDTO itemRequestToResponseDto(ItemRequest itemRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest dtoToItemRequest(ItemRequestDTO dto);
}
