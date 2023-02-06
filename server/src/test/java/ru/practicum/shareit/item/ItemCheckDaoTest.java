package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.NoObjectsFoundException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dao.ItemCheckDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemCheckDaoTest {

    private ItemRepository itemRepository;
    private ItemCheckDao itemCheckDao;
    private Item item;
    private User user;

    @BeforeEach
    public void defaultSetUp() {
        itemRepository = mock(ItemRepository.class);
        itemCheckDao = new ItemCheckDao(itemRepository);
        user = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        item = Item.builder()
                .id(1)
                .name("Пила")
                .description("Ручная пила")
                .owner(user)
                .build();
    }

    @Test
    void checkItemNotExist() {
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownIdException.class, () -> itemCheckDao.checkItemExist(1));
    }

    @Test
    void checkUserHasNoAccessToItem() {
        Assertions.assertThrows(NoObjectsFoundException.class, () -> itemCheckDao.checkUserAccessToItem(2, item));
    }

    @Test
    void checkItemAvailability() {
        item.setAvailable(false);
        when(itemRepository.getById(anyInt()))
                .thenReturn(item);
        Assertions.assertThrows(ItemUnavailableException.class, () -> itemCheckDao.checkItemAvailability(1));
    }
}