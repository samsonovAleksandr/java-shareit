package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserMapper {

    public UserDto toUser(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public List<UserDto> toUserList(List<User> userList) {
        ArrayList<UserDto> userDto = new ArrayList<>();
        for (User user : userList) {
            userDto.add(toUser(user));
        }
        return userDto;
    }
}
