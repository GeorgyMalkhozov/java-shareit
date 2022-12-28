package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = UserMapper.class, componentModel = "spring")
public interface ItemMapper {

    ItemDTO itemToItemDto(Item item);

    Item itemDtoToItem(ItemDTO dto);

    Item itemUpdateDtoToItem(ItemUpdateDTO itemUpdateDTO);

}
