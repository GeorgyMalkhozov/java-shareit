package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    private Item item;
    private User user;
    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    public void defaultSetUp() {

        user = User.builder()
                .id(1)
                .name("Петр")
                .email("petr@mail.ru")
                .build();
        item = Item.builder()
                .id(1)
                .name("Пила")
                .owner(user)
                .available(true)
                .build();
        comment = Comment.builder()
                .id(1)
                .text("Отзыв")
                .author(user)
                .item(item)
                .build();
        commentDTO = CommentDTO.builder()
                .text("Отзыв")
                .build();
    }

    @Test
    void commentToCommentForItemResponseEntityDto() {
        CommentResponseDTO commentResponseDTO = commentMapper.commentToCommentForItemResponseEntityDto(comment);
        Assertions.assertNotNull(commentResponseDTO);
        Assertions.assertEquals(comment.getText(), commentResponseDTO.getText());
    }

    @Test
    void commentDtoToComment() {
        Comment comment1 = commentMapper.commentDtoToComment(commentDTO);
        Assertions.assertNotNull(comment1);
        Assertions.assertEquals(commentDTO.getText(), comment1.getText());
    }

    @Test
    void workWithNull() {
        Assertions.assertNull(commentMapper.commentDtoToComment(null));
        Assertions.assertNull(commentMapper.commentToCommentForItemResponseEntityDto(null));
    }
}