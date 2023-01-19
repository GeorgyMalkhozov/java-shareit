package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, CommentMapper.class}, componentModel = "spring")
public interface ItemMapper {

    ItemResponseDTO itemToItemResponseDto(Item item);

    Item itemDtoToItem(ItemDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemUpdateDTO itemUpdateDTO, @MappingTarget Item item);
}
