package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestBody @Valid ItemRequestDTO dto,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestClient.addItemRequest(dto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable Integer requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestClient.getItemRequest(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestClient.findAllRequestsByCurrentUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findOthersItemRequests(
             @RequestHeader("X-Sharer-User-Id") Integer userId,
             @RequestParam(defaultValue = "0") @Min(0) Integer from,
             @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return requestClient.findOthersItemRequests(userId, from, size);
    }
}