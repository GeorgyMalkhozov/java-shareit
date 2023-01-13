package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT it " +
            "FROM Item AS it " +
            "WHERE ((LOWER(it.name) LIKE %:text% OR LOWER(it.description) LIKE %:text%) AND it.available = true) ")
    List<Item> searchItemsContainsText(@Param("text") String text);

    List<Item> findAllByOwnerId(Integer ownerId);

}
