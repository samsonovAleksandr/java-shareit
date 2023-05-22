package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.ArrayList;

public interface UserService {

    User addUser(User user);

    User getUserId(int id);

    void userDelete(int id);

    ArrayList<User> getAllUser();

    User updateUser(User user, int id);
}
