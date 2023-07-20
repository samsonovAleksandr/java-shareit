package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemEntityRepositoryTest {

    @Autowired
    private ItemEntityRepository itemRepository;

    @Autowired
    private UserEntityRepository userRepository;

    private User user;

    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Sima")
                .email("sima@sima.ru")
                .build();
        item = Item.builder()
                .name("Drill")
                .available(true)
                .owner(user)
                .description("Mini Drill")
                .build();
    }

    @Test
    void search() {
        userRepository.save(user);
        itemRepository.save(item);
        List<Item> itemList = itemRepository.search("Drill");
        assertEquals(itemList.size(), 1);
    }

}