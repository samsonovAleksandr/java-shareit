package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserEntityRepository userEntityRepository;

    public User addUser(User user) {
        userEntityRepository.save(user);
        return userEntityRepository.getReferenceById(user.getId());
    }

    public User getUserId(long id) {

        if (userEntityRepository.existsById(id)) {
            return userEntityRepository.getReferenceById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }

    }

    public void userDelete(long id) {
        userEntityRepository.deleteById(id);
    }

    public ArrayList<User> getAllUser() {
        return new ArrayList<>(userEntityRepository.findAll());
    }

    public User updateUser(User user, long id) {
        User user1 = userEntityRepository.getReferenceById(id);
        if (user.getEmail() != null) user1.setEmail(user.getEmail());

        if (user.getName() != null) user1.setName(user.getName());

        userEntityRepository.save(user1);
        return user1;
    }
}
