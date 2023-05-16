package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
@Service
public class UserMapper {

    public static User toUser (User user) {
        return new User (
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
