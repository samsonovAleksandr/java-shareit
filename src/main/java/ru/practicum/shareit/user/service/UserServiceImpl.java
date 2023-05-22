package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    private int i = 0;
    public HashMap<Integer, User> users = new HashMap<>();

    private int newId() {
        return ++i;
    }

    public User addUser(User user) {
        for (User user1 : users.values()) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT);
            }
        }
        user.setId(newId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public User getUserId(int id) {
        return users.get(id);
    }

    public void userDelete(int id) {
        users.remove(id);
    }

    public ArrayList<User> getAllUser() {
        return new ArrayList<>(users.values());
    }

    public User updateUser(User user, int id) {
        for (User us : users.values()) {
            if (us.getEmail().equals(user.getEmail()) && us.getId() != id) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT);
            }
        }
        if (user.getName() != null) users.get(id).setName(user.getName());

        if (user.getEmail() != null) users.get(id).setEmail(user.getEmail());

        return users.get(id);
    }
}
