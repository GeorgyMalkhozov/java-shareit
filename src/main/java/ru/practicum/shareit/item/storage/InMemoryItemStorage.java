package ru.practicum.shareit.item.storage;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.NoItemsFoundBySearchException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Repository
public class InMemoryItemStorage implements ItemStorage {

    private static int itemIdGenerator;
    private final Map<Integer, Item> items = new HashMap<>();

    public Item addItem(Item item) {
        item.setId(generateNewItemId());
        items.put(item.getId(), item);
        return item;
    }

    public Item getItem(Integer id) {
        return items.get(id);
    }

    public List<Item> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> foundItems = items.values().stream()
                .filter(Item::getAvailable)
                .filter(str -> (str.getName().toLowerCase().contains(text.toLowerCase()))
                        || (str.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());

        if (foundItems.isEmpty()) {
            throw new NoItemsFoundBySearchException("Не найдены вещи содержащие '" + text + "'");
        } else {
            return foundItems;
        }
    }

    public Item putItem(Item item) {
        items.put(item.getId(),item);
        return item;
    }

    public void deleteItem(Integer itemId) {
        items.remove(itemId);
    }

    public List<Item> findAllItems(Integer userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    private static int generateNewItemId() {
        itemIdGenerator++;
        return itemIdGenerator;
    }

    public Item updateItem(Integer id, Item item) {

        Item oldItem = items.get(id);

        if (item.getName() != null && !Objects.equals(oldItem.getName(), item.getName())) {
            items.get(id).setName(item.getName());
        }
        if (item.getDescription() != null && !Objects.equals(oldItem.getDescription(), item.getDescription())) {
            items.get(id).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null && !Objects.equals(oldItem.getAvailable(), item.getAvailable())) {
            items.get(id).setAvailable(item.getAvailable());
        }
        return oldItem;
    }

    public void checkItemId(Integer id) {
        if (!items.containsKey(id)) {
            throw new UnknownIdException("Указан некорректный Id вещи");
        }
    }

    public void checkUserAccessToItem(Integer userId, Item item) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new ItemAccessDeniedException("Только владелец вещи может ее изменять");
        }
    }
}
