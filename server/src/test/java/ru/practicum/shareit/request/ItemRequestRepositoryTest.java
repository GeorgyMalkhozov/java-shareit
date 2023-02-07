package ru.practicum.shareit.request;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;

    private User requestor1;
    private User requestor2;


    public void setUp() throws Exception {

        requestor1 = User.builder()
                .name("Иван")
                .email("ivan@mail.ru")
                .build();
        userRepository.save(requestor1);

        requestor2 = User.builder()
                .name("Сергей")
                .email("sergey@mail.ru")
                .build();
        userRepository.save(requestor2);

        request1 = ItemRequest.builder()
                .requestor(requestor1)
                .description("Нужна ручная пила")
                .created(LocalDateTime.now().minusHours(2))
                .build();
        requestRepository.save(request1);

        request2 = ItemRequest.builder()
                .requestor(requestor2)
                .description("Нужна лестница")
                .created(LocalDateTime.now().minusHours(5))
                .build();
        requestRepository.save(request2);

        request3 = ItemRequest.builder()
                .requestor(requestor1)
                .description("Нужна отвертка")
                .created(LocalDateTime.now().minusHours(11))
                .build();
        requestRepository.save(request3);
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assert.assertNotNull(entityManager);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(requestRepository);
    }

    @Test
    void saveRequest() {
        request1 = ItemRequest.builder()
                .requestor(requestor1)
                .description("Нужна ручная пила")
                .created(LocalDateTime.now().minusHours(2))
                .build();
        requestRepository.save(request1);
        Assert.assertNotNull(request1.getId());
        Assert.assertEquals(request1.getDescription(), requestRepository.getById(request1.getId()).getDescription());
    }

    @Test
    void findAllByRequestorIdOrderByCreatedDesc() throws Exception {
        setUp();
        Assert.assertEquals(List.of(request1, request3),
                requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestor1.getId()));
    }

    @Test
    void findAllByRequestorIdNotOrderByCreatedDesc() throws Exception {
        setUp();
        Assert.assertEquals(List.of(request2),
                requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(requestor1.getId(),
                        PageRequest.of(0,5)).toList());
    }

}