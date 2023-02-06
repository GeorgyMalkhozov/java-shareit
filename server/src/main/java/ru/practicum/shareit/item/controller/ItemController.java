package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemResponseDTO addItem(@RequestBody @Valid ItemDTO dto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.addItem(dto, userId);
    }

    @GetMapping("/{id}")
    public ItemResponseDTO getItem(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItem(id, userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDTO> searchItems(@RequestParam String text,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PutMapping
    public ItemResponseDTO putItem(@Valid @RequestBody ItemDTO dto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.putItem(dto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        itemService.deleteItem(id, userId);
    }

    @GetMapping
    public List<ItemResponseDTO> findAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return itemService.findAllItems(userId, from, size);
    }

    @PatchMapping("/{id}")
    public ItemResponseDTO updateItem(@PathVariable Integer id, @RequestBody @Valid ItemUpdateDTO updateDto,
                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.updateItem(id, updateDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDTO addComment(@RequestBody @Valid CommentDTO dto, @PathVariable Integer itemId,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.addComment(dto, itemId, userId);
    }
}
