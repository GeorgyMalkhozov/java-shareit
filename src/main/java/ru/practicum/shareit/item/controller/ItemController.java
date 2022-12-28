package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDTO addItem(@RequestBody @Valid ItemDTO dto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.addItem(dto, userId);
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }

    @GetMapping("/search")
    public List<ItemDTO> searchItems(@RequestParam (defaultValue = "2") String text) {
        return itemService.searchItems(text);
    }

    @PutMapping
    public ItemDTO putItem(@Valid @RequestBody ItemDTO dto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.putItem(dto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        itemService.deleteItem(id, userId);
    }

    @GetMapping
    public List<ItemDTO> findAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.findAllItems(userId);
    }

    @PatchMapping("/{id}")
    public ItemDTO updateItem(@PathVariable Integer id, @RequestBody @Valid ItemUpdateDTO updateDto,
                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.updateItem(id, updateDto, userId);
    }
}
