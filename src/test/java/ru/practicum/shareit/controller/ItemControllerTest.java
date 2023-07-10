package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private CommentDto commentDto;

    private Comment comment;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    ItemMapper itemMapper;

    @MockBean
    ItemService itemService;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User(1, "user", "user@user.com");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        Item item = Item.builder()
                .id(1)
                .name("Drill")
                .description("Drill Mini")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("Nice")
                .authorName(user.getName())
                .item(item)
                .created(LocalDateTime.now())
                .build();

        comment = commentMapper.toComment(commentDto, item, user);
        comment.setId(1);
    }

    @Test
    public void shouldItemWithoutXSharerUserId() throws Exception {
        Item item = new Item(2, "Drill", "Simple drill", true, user, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWithoutAvailable() throws Exception {
        Item item = new Item(2, "Drill", "Simple drill", null, user, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWitEmptyName() throws Exception {
        Item item = new Item(2, "", "Simple drill", true, user, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemWitEmptyDescription() throws Exception {
        Item item = new Item(2, "Drill", "", true, user, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldItemUpdateWithoutXSharerUserId() throws Exception {
        Item item = new Item(1, "Drill+", "cordless drill", false, user, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postComment() throws Exception {
        when(itemService.postComment(any(), anyLong(), anyLong())).thenReturn(comment);
        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value("user"))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value("Drill"))
                .andExpect(jsonPath("$.item.description").value("Drill Mini"));
    }

    @Test
    public void shouldPostCommentWithoutXSharerUserId() throws Exception {
        when(itemService.postComment(any(), anyLong(), anyLong())).thenReturn(comment);
        mvc.perform(post("/items/1/comment")
                .content(objectMapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void createItem() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Item item2 = Item.builder()
                .id(1)
                .name("Drill")
                .description("Mini Drill")
                .available(true)
                .build();
        when(itemService.create(any(), anyLong())).thenReturn(item);
        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(item2))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name").value("Drill"))
                .andExpect(jsonPath("$.description").value("Mini Drill"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.id").value(1))
                .andExpect(jsonPath("$.owner.name").value("user"))
                .andExpect(jsonPath("$.owner.email").value("user@user.com"));
    }


    @Test
    public void shouldGetItem() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        ItemDtoCommentResponse idcr = itemMapper.toItemResponseDto(item, List.of(booking), List.of(comment));
        when(itemService.get(anyLong(), anyLong())).thenReturn(idcr);
        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drill"))
                .andExpect(jsonPath("$.description").value("Mini Drill"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.name").value("user"))
                .andExpect(jsonPath("$.owner.id").value(1));
    }

    @Test
    public void shouldGetItemWithoutIdItem() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        ItemDtoCommentResponse idcr = itemMapper.toItemResponseDto(item, List.of(booking), List.of(comment));
        when(itemService.get(anyLong(), anyLong())).thenReturn(idcr);
        mvc.perform(get("/items/99")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetItemWithoutXSharerUserId() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        ItemDtoCommentResponse idcr = itemMapper.toItemResponseDto(item, List.of(booking), List.of(comment));
        when(itemService.get(anyLong(), anyLong())).thenReturn(idcr);
        mvc.perform(get("/items/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetItemOwnerWithoutXSharerUserId() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        ItemDtoCommentResponse idcr = itemMapper.toItemResponseDto(item, List.of(booking), List.of(comment));
        when(itemService.getItemOwner(anyLong())).thenReturn(List.of(idcr));
        mvc.perform(get("/items")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetItemOwner() throws Exception {
        Item item = new Item(1, "Drill", "Mini Drill", true, user, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        ItemDtoCommentResponse idcr = itemMapper.toItemResponseDto(item, List.of(booking), List.of(comment));
        when(itemService.getItemOwner(anyLong())).thenReturn(List.of(idcr));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Drill"))
                .andExpect(jsonPath("$.[0].description").value("Mini Drill"))
                .andExpect(jsonPath("$.[0].comments.[0].id").value(1));
    }

}
