package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    @Autowired
    UserService userService;

    @Test
    void addUser() {
        User user = User.builder()
                .name("Sima")
                .email("sima@mail.ru").build();
        userService.addUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void patchUser() {
        User user = User.builder()
                .name("Sima")
                .email("sima2@mail.ru").build();
        userService.addUser(user);
        User upUser = User.builder()
                .id(1)
                .name("Sima")
                .email("sima1@mail.ru").build();
        userService.updateUser(upUser, 1);
        AssertionsForClassTypes.assertThat(userService.getUserId(upUser.getId()))
                .hasFieldOrPropertyWithValue("id", upUser.getId());
    }

    @Test
    void notFoundUser() {
        User user = User.builder()
                .id(9999)
                .name("Clown")
                .email("clown@baza.ru")
                .build();
        Assertions.assertThatThrownBy(() -> userService.updateUser(user, user.getId()));
    }

    @Test
    void getAllUsers() {
        User user = User.builder()
                .name("Simka")
                .email("sima5@mail.ru")
                .build();
        userService.addUser(user);
        Collection<User> users = userService.getAllUser();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
    }
}
