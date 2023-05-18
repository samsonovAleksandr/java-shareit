package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;

public interface UserService {

    UserDto addUser(User user);

    UserDto getUserId(int id);

    void userDelete(int id);

    ArrayList<User> getAllUser();

    UserDto updateUser(User user, int id);
}
