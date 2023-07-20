package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    private final UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
    }

    @Test
    @Transactional
    void addUser() {
        userService.addUser(user);
        assertEquals(user.getId(), 1);
    }

    @Test
    void getUserId() {
        userService.addUser(user);
        User user1 = userService.getUserId(1);
        assertEquals(user1.getId(), 1);
    }

    @Test
    void getUserIdUserNotFound() {
        userService.addUser(user);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.getUserId(99));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void userDelete() {
        userService.addUser(user);
        userService.userDelete(1);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.getUserId(1));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void getAllUser() {
        userService.addUser(user);
        User user1 = User.builder()
                .name("Dasha")
                .email("Dasha@mail.com")
                .build();
        userService.addUser(user1);
        List<User> userList = userService.getAllUser();
        assertEquals(userList.size(), 2);
    }

    @Test
    void updateUser() {
        userService.addUser(user);
        User user1 = User.builder()
                .name("SimaUpd")
                .email("simaUpd@mail.com")
                .build();
        User user2 = userService.updateUser(user1, 1);
        assertEquals(user2.getId(), 1);
        assertEquals(user2.getName(), "SimaUpd");
    }
}