package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    User user = User.builder()
            .name("Sima")
            .email("sima@mail.ru").build();
    Item item = Item.builder()
            .name("Drel")
            .description("dl9 doma")
            .available(true)
            .owner(user)
            .request(null)
            .build();

    @Test
    void itemCreate() {
        userService.addUser(user);
        itemService.postItem(item, 1);
        AssertionsForClassTypes.assertThat(item).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(item).extracting("name").isNotNull();
    }

    @Test
    void itemPatch() {
        Item item1 = Item.builder()
                .name("Drel11")
                .description("dl9 doma111")
                .available(false)
                .owner(user)
                .request(null)
                .build();
        itemService.patchItem(1, 1, item1);
        Assertions.assertEquals(false, itemService.getItem(1, 1).getAvailable());
        Item item2 = Item.builder()
                .name("Drel")
                .description("dl9 doma")
                .available(true)
                .owner(user)
                .request(null)
                .build();
        itemService.patchItem(1, 1, item2);
        Assertions.assertEquals(true, itemService.getItem(1, 1).getAvailable());
        Assertions.assertEquals("Drel", itemService.getItem(1, 1).getName());
    }

    @Test
    void getItemOwner() {
        Assertions.assertEquals(itemService.getItemOwner(1).size(), 1);
    }

}
