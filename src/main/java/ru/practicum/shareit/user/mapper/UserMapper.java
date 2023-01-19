package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserResponseDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDto(User user);

    UserResponseDTO userToUserResponseDto(User user);

    User userDtoToUser(UserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDTO userUpdateDTO, @MappingTarget @Validated User user);
}
