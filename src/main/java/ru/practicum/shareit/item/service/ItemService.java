package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    public ItemDTO addItem(ItemDTO itemDto, Integer userId) {
        Item item = itemMapper.itemDtoToItem(itemDto);
        addOwnerToItem(item, userId);
        itemStorage.addItem(item);
        return itemMapper.itemToItemDto(item);
    }

    public ItemDTO getItem(Integer id) {
        itemStorage.checkItemId(id);
        return itemMapper.itemToItemDto(itemStorage.getItem(id));
    }

    public List<ItemDTO> searchItems(String text) {
        return itemStorage.searchItems(text).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    public ItemDTO putItem(ItemDTO itemDto, Integer userId) {
        checkItem(itemDto.getId(), userId);
        Item newItem = itemStorage.addItem(itemMapper.itemDtoToItem(itemDto));
        return itemMapper.itemToItemDto(itemStorage.putItem(newItem));
    }

    public void deleteItem(Integer id, Integer userId) {
        checkItem(id, userId);
        itemStorage.deleteItem(id);
    }

    public List<ItemDTO> findAllItems(Integer userId) {
        return itemStorage.findAllItems(userId).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    public ItemDTO updateItem(Integer id, ItemUpdateDTO dto, Integer userId) {
        checkItem(id, userId);
        Item item = itemMapper.itemUpdateDtoToItem(dto);
        return itemMapper.itemToItemDto(itemStorage.updateItem(id, item));
    }

    private void addOwnerToItem(Item item, Integer userId) {
        item.setOwner(userStorage.getUser(userId));
    }

    private void checkItem(Integer itemId, Integer userId) {
        itemStorage.checkItemId(itemId);
        itemStorage.checkUserAccessToItem(userId, itemStorage.getItem(itemId));
    }
}
