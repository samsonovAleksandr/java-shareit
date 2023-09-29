package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable int id) {
        return userMapper.toUser(userService.getUserId(id));
    }

    @PostMapping
    public UserDto postUser(@RequestBody User user) {
        return userMapper.toUser(userService.addUser(user));
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        userService.userDelete(id);
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        return userMapper.toUserList(userService.getAllUser());
    }

    @PatchMapping("{id}")
    public UserDto putUser(@RequestBody User user,
                           @PathVariable int id) {
        return userMapper.toUser(userService.updateUser(user, id));
    }

}
