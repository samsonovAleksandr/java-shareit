package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService{

    private int i = 0;
   public HashMap<Integer, User> users = new HashMap<>();

    private int newId() {
        return ++i;
    }

    UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserDto addUser(User user) {
        for (User us : users.values()) {
            if (us.getEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT);
            }
        }
        user.setId(newId());
        users.put(user.getId(), user);
        return userMapper.toUser(users.get(user.getId()));
    }

    public UserDto getUserId(int id) {
        return userMapper.toUser(users.get(id));
    }

    public void userDelete(int id) {
        users.remove(id);
    }

    public ArrayList<User> getAllUser() {
        return new ArrayList<>(users.values());
    }

    public UserDto updateUser(User user, int id) {
        for (User us : users.values()){
            if (us.getEmail().equals(user.getEmail()) && us.getId() != id){
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT);
            }
        }
        if (user.getName() != null) users.get(id).setName(user.getName());

        if (user.getEmail() != null) users.get(id).setEmail(user.getEmail());

        return userMapper.toUser(users.get(id));
    }
}
