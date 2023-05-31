package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.ArrayList;

public interface UserService {

    User addUser(User user);

    User getUserId(long id);

    void userDelete(long id);

    ArrayList<User> getAllUser();

    User updateUser(User user, long id);
}
