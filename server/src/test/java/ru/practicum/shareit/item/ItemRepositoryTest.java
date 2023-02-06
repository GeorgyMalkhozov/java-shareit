package ru.practicum.shareit.item;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private Item item1;
    private Item item2;
    private Item item3;

    private User user1;

    private ItemRequest request1;

    public void setUp() throws Exception {
        item1 = Item.builder()
                .name("Пила")
                .description("Ручная пила")
                .available(true)
                .build();
        itemRepository.save(item1);

        item2 = Item.builder()
                .name("Пила")
                .description("Циркулярная пила")
                .available(true)
                .build();
        itemRepository.save(item2);

        item3 = Item.builder()
                .name("Велосипед")
                .description("Трехколесный велосипед")
                .available(true)
                .build();
        itemRepository.save(item3);

        user1 = User.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userRepository.save(user1);
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assert.assertNotNull(entityManager);
        Assert.assertNotNull(itemRepository);
    }

    @Test
    void saveItem() {
        item1 = Item.builder()
                .name("Пила")
                .description("Ручная пила")
                .available(true)
                .build();
        itemRepository.save(item1);
        Assert.assertNotNull(item1.getId());
        Assert.assertEquals(item1.getName(), itemRepository.getById(item1.getId()).getName());
    }

    @Test
    void searchItemsContainsText() throws Exception {
        setUp();
        Assert.assertEquals(List.of(item1, item2), itemRepository.searchItemsContainsText("пила", PageRequest.of(0,5)).toList());
    }

    @Test
    void findAllByOwnerIdOrderByIdAsc() throws Exception {
        setUp();
        item2.setOwner(user1);
        Assert.assertEquals(List.of(item2), itemRepository.findAllByOwnerIdOrderByIdAsc(user1.getId(), PageRequest.of(0,5)).toList());
    }

    @Test
    void findAllByRequestIdOrderByIdAsc() throws Exception {
        setUp();
        request1 = ItemRequest.builder()
                .description("Запрос")
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(request1);
        item3.setRequest(request1);
        Assert.assertEquals(List.of(item3), itemRepository.findAllByRequestIdOrderByIdAsc(request1.getId()));
    }
}