package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentResponseDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, ItemMapper.class}, componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id")
    @Mapping(target = "item")
    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "created")
    CommentResponseDTO commentToCommentForItemResponseEntityDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment commentDtoToComment(CommentDTO dto);
}
