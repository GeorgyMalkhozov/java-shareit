package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.NoObjectsFoundException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Objects;

@Component
public class ItemCheckDao {

    private final ItemRepository itemRepository;

    public ItemCheckDao(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void checkItemExist(Integer itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new UnknownIdException("Вещь с id = " + itemId + " не существует");
        }
    }

    public void checkUserAccessToItem(Integer userId, Item item) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NoObjectsFoundException("Только владелец вещи может ее изменять");
        }
    }

    public void checkItemAvailability(Integer itemId) {
        if (Boolean.FALSE.equals(itemRepository.getById(itemId).getAvailable())) {
            throw new ItemUnavailableException("Вещь недоступна для аренды");
        }
    }
}
