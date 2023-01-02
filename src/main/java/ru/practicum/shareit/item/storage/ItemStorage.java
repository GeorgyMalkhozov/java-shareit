package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item getItem(Integer id);

    List<Item> searchItems(String text);

    Item putItem(Item item);

    void deleteItem(Integer itemId);

    List<Item> findAllItems(Integer userId);

    Item updateItem(Integer id, Item item);

    void checkItemId(Integer id);

    void checkUserAccessToItem(Integer userId, Item item);
}
