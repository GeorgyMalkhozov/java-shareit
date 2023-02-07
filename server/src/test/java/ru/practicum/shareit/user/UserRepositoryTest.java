package ru.practicum.shareit.user;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

   @Test
   void injectedComponentsAreNotNull() {
       Assert.assertNotNull(entityManager);
       Assert.assertNotNull(userRepository);
   }

    @Test
    void saveUser() {
        user = User.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userRepository.save(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(user.getName(), userRepository.getById(user.getId()).getName());
    }

    @Test
    void testDuplicateUser() {
        user = User.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        userRepository.save(user);

        User user1 = User.builder()
                .name("Петр")
                .email("petr@mail.ru")
                .build();

        Exception thrown = Assertions
                .assertThrows(DataIntegrityViolationException.class, () -> {
                    userRepository.save(user1);
                }, "DataIntegrityViolationException exception was expected");
    }
}