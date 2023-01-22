package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, CommentMapper.class}, componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "requestId", source = "item.request.id")
    ItemResponseDTO itemToItemResponseDto(Item item);

    @Mapping(target = "request", ignore = true)
    Item itemDtoToItem(ItemDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemUpdateDTO itemUpdateDTO, @MappingTarget Item item);
}
