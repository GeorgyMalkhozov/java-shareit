package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestResponseDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestMapper requestMapper;

    @Autowired
    public ItemRequestService(ItemRequestRepository itemRequestRepository,
                              ItemService itemService, UserService userService, ItemRequestMapper requestMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemService = itemService;

        this.userService = userService;
        this.requestMapper = requestMapper;
    }

    public ItemRequestResponseDTO addItemRequest(ItemRequestDTO dto, Integer userId) {
        userService.checkUserIdExist(userId);
        ItemRequest itemRequest = requestMapper.dtoToItemRequest(dto);
        addRequestorToRequest(itemRequest, userId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return requestMapper.itemRequestToResponseDto(itemRequest);
    }

    public ItemRequestResponseDTO getItemRequest(Integer id, Integer userId) {
        checkRequestIdExist(id);
        userService.checkUserIdExist(userId);
        ItemRequest itemRequest = itemRequestRepository.getById(id);
        ItemRequestResponseDTO responseDTO = requestMapper.itemRequestToResponseDto(itemRequest);
        return enrichRequestWithItemsMatched(responseDTO);
    }

  //  @Transactional(readOnly = true)
    public List<ItemRequestResponseDTO> findAllItemRequestsByCurrentUser(Integer userId) {
        userService.checkUserIdExist(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        if (itemRequests.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRequests.stream()
                    .map(requestMapper::itemRequestToResponseDto)
                    .map(this::enrichRequestWithItemsMatched)
                    .collect(Collectors.toList());
        }
    }

    public List<ItemRequestResponseDTO> findOthersItemRequests(Integer userId, Integer from, Integer size) {
        userService.checkUserIdExist(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.
                findAllByRequestorIdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size)).toList();
        return itemRequests.stream()
                .map(requestMapper::itemRequestToResponseDto)
                .map(this::enrichRequestWithItemsMatched)
                .collect(Collectors.toList());
    }

    public ItemRequest getRequestById(Integer id) {
        checkRequestIdExist(id);
        return itemRequestRepository.getById(id);
    }

    public void checkRequestIdExist(Integer requestId) {
        if (itemRequestRepository.findById(requestId).isEmpty()) {
            throw new UnknownIdException("Запрос с id = " + requestId + " не существует");
        }
    }

    private void addRequestorToRequest(ItemRequest itemRequest, Integer userId) {
        itemRequest.setRequestor(userService.getUserById(userId));
    }

    private ItemRequestResponseDTO enrichRequestWithItemsMatched(ItemRequestResponseDTO dto) {
        dto.setItems(itemService.getItemsToItemRequest(dto.getId()));
        return dto;
    }
}
