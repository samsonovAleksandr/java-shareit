package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@AllArgsConstructor
public class UserMapper {

    public UserDto toUser (User user) {
        return new UserDto (
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
