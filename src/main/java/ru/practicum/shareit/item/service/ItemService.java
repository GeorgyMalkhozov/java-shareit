package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.NoObjectsFoundException;
import ru.practicum.shareit.item.dao.ItemCheckDao;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemCheckDao itemCheckDao;

    @Autowired
    public ItemService(ItemRepository itemRepository, CommentRepository commentRepository,
                       ItemRequestRepository itemRequestRepository, UserService userService,
                       ItemMapper itemMapper, BookingService bookingService, CommentMapper commentMapper,
                       ItemCheckDao itemCheckDao) {
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.bookingService = bookingService;
        this.commentMapper = commentMapper;
        this.itemCheckDao = itemCheckDao;
    }

    @Transactional
    public ItemResponseDTO addItem(ItemDTO itemDto, Integer userId) {
        userService.checkUserIdExist(userId);
        Item item = itemMapper.itemDtoToItem(itemDto);
        addOwnerToItem(item, userId);
        addRequestToItem(item, itemDto.getRequestId());
        itemRepository.save(item);
        return itemMapper.itemToItemResponseDto(item);
    }

    @Transactional(readOnly = true)
    public ItemResponseDTO getItem(Integer id, Integer userId) {
        itemCheckDao.checkItemExist(id);
        ItemResponseDTO itemResponseDto = itemMapper.itemToItemResponseDto(itemRepository.getById(id));
        itemResponseDto.setComments(getCommentsOfItem(itemResponseDto.getId()));
        if (itemResponseDto.getOwner().getId().equals(userId)) {
            bookingService.addLastAndNextBookingsToItem(itemResponseDto);
        }
        return itemResponseDto;
    }

    @Transactional
    public ItemResponseDTO updateItem(Integer id, ItemUpdateDTO dto, Integer userId) {
        checkItem(id, userId);
        Item item = itemRepository.getById(id);
        itemMapper.updateItemFromDto(dto, item);
        itemRepository.save(item);
        return itemMapper.itemToItemResponseDto(item);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDTO> searchItems(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemResponseDTO> foundItems = itemRepository
                .searchItemsContainsText(text.toLowerCase(), PageRequest.of(from / size, size)).toList().stream()
                .map(itemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
        if (foundItems.isEmpty()) {
            throw new NoObjectsFoundException("Не найдены вещи содержащие '" + text + "'");
        } else {
            return foundItems;
        }
    }

    @Transactional
    public ItemResponseDTO putItem(ItemDTO itemDto, Integer userId) {
        checkItem(itemDto.getId(), userId);
        return itemMapper.itemToItemResponseDto(itemRepository.save(itemMapper.itemDtoToItem(itemDto)));
    }

    @Transactional
    public void deleteItem(Integer id, Integer userId) {
        checkItem(id, userId);
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDTO> findAllItems(Integer userId, Integer from, Integer size) {
        List<ItemResponseDTO> list = itemRepository
                .findAllByOwnerIdOrderByIdAsc(userId, PageRequest.of(from / size, size)).toList()
                .stream()
                .map(itemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
        list.forEach(bookingService::addLastAndNextBookingsToItem);
        return list;
    }

    @Transactional
    public CommentResponseDTO addComment(CommentDTO commentDto, Integer itemId, Integer userId) {
        preAddCommentCheck(itemId, userId);
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        enrichCommentWithDataAfterAddition(itemId, userId, comment);
        commentRepository.save(comment);
        return commentMapper.commentToCommentForItemResponseEntityDto(comment);
    }

    private void enrichCommentWithDataAfterAddition(Integer itemId, Integer userId, Comment comment) {
        addAuthorToComment(comment, userId);
        addItemToComment(comment, itemId);
        comment.setCreated(LocalDateTime.now());
    }

    private void preAddCommentCheck(Integer itemId, Integer userId) {
        userService.checkUserIdExist(userId);
        itemCheckDao.checkItemExist(itemId);
        bookingService.checkCommentatorWasABookerBeforeComment(itemId, userId);
        checkRepeatedCommentByAuthor(itemId, userId);
    }

    private void addOwnerToItem(Item item, Integer userId) {
        item.setOwner(userService.getUserById(userId));
    }

    private void addRequestToItem(Item item, Integer requestId) {
        if (requestId != null) {
            item.setRequest(itemRequestRepository.getById(requestId));
        }
    }

    private void addAuthorToComment(Comment comment, Integer userId) {
        comment.setAuthor(userService.getUserById(userId));
    }

    private void addItemToComment(Comment comment, Integer itemId) {
        comment.setItem(itemRepository.getById(itemId));
    }

    private List<CommentResponseDTO> getCommentsOfItem(Integer itemId) {
       return commentRepository.findAllByItemId(itemId).stream()
                .map(commentMapper::commentToCommentForItemResponseEntityDto)
                .collect(Collectors.toList());
    }

    private void checkItem(Integer itemId, Integer userId) {
        itemCheckDao.checkItemExist(itemId);
        itemCheckDao.checkUserAccessToItem(userId, itemRepository.getById(itemId));
    }

    private void checkRepeatedCommentByAuthor(Integer itemId, Integer userId) {
        if (!commentRepository.findAllByItemIdAndAuthorId(itemId, userId).isEmpty()) {
            throw new DuplicateDataException("Пользователь уже осталвял отзвыв на эту вещь ранее");
        }
    }

    public List<ItemResponseDTO> getItemsToItemRequest(Integer requestId) {
        return itemRepository.findAllByRequestIdOrderByIdAsc(requestId).stream()
                .map(itemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
    }
}
