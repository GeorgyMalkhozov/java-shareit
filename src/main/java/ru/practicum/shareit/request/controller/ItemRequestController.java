package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @Autowired
    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestResponseDTO addItemRequest(@RequestBody @Valid ItemRequestDTO dto,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.addItemRequest(dto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDTO getItemRequest(@PathVariable Integer requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getItemRequest(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestResponseDTO> findAllRequestsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.findAllItemRequestsByCurrentUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDTO> findOthersItemRequests
            (@RequestHeader("X-Sharer-User-Id") Integer userId,
             @RequestParam(defaultValue = "0") @Min(0) Integer from,
             @RequestParam(defaultValue = "5") @Min(0) Integer size) {
        return requestService.findOthersItemRequests(userId, from, size);
    }
}
