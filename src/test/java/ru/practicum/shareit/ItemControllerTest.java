package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ItemService itemService;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User(1L, "user", "user@user.com");
        String jsonUser = objectMapper.writeValueAsString(user);


        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser));

        Item item = new Item(1L, "Дрель", "Простая дрель", user, true);
        String jsonItem = objectMapper.writeValueAsString(item);

        Long userId = 1L;
        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonItem));
    }

    @Test
    public void shouldItemWithoutXSharerUserId() throws Exception {
        Item item = new Item(2L, "Дрель", "Простая дрель", user, true);
        String jsonItem = objectMapper.writeValueAsString(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWithNotFoundUser() throws Exception {
        Item item = new Item(2L, "Дрель", "Простая дрель", user, true);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 99L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWithoutAvailable() throws Exception {
        Item item = new Item(2L, "Дрель", "Простая дрель", user, null);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 2L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWitEmptyName() throws Exception {
        Item item = new Item(2L, "", "Простая дрель", user, true);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 2L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWitEmptyDescription() throws Exception {
        Item item = new Item(2L, "Дрель", "", user, true);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 2L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemUpdateWithoutXSharerUserId() throws Exception {
        Item item = new Item(1L, "Дрель+", "Аккумуляторная дрель", user, false);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 1L;

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }
}
