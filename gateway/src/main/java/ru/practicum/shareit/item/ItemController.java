package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDTO dto,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.addItem(dto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.getItem(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return itemClient.searchItems(text, userId, from, size);
    }

    @PutMapping
    public ResponseEntity<Object> putItem(@Valid @RequestBody ItemDTO dto,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.putItem(dto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        itemClient.deleteItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return itemClient.findAllItems(userId, from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable Integer id, @RequestBody @Valid ItemUpdateDTO updateDto,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.updateItem(id, updateDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDTO dto, @PathVariable Integer itemId,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.addComment(dto, itemId, userId);
    }
}
