package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {
    private final ItemEntityRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final UserEntityRepository userRepository;

    private final ItemService itemService;

    private User user;

    private Item item;

    private Comment comment;


    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@test.com")
                .build();
        userRepository.save(user);

        item = Item.builder()
                .name("Drill")
                .description("Mini Drill")
                .available(true)
                .build();

    }

    @Test
    @Transactional
    void create() {
        Item item1 = itemService.create(item, user.getId());

        assertNotNull(item1);
        assertEquals(user.getId(), item1.getOwner().getId());
    }

    @Test
    @Transactional
    void createUserNotFound() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.create(item, 99));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void postComment() {
        item.setOwner(user);
        itemRepository.save(item);
        User user1 = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        userRepository.save(user1);
        Booking booking = Booking.builder()
                .booker(user1)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking);
        CommentDto commentDto = CommentDto.builder()
                .text("Nice")
                .authorName("Sima")
                .item(item)
                .created(LocalDateTime.now())
                .build();
        comment = itemService.postComment(commentDto, item.getId(), user1.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getItem(), item);
        assertEquals(comment.getAuthorName(), user1.getName());
    }

    @Test
    void postCommentBookingUserStatus() {
        item.setOwner(user);
        itemRepository.save(item);
        User user1 = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        userRepository.save(user1);
        Booking booking = Booking.builder()
                .booker(user1)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.REJECTED)
                .build();
        bookingRepository.save(booking);
        CommentDto commentDto = CommentDto.builder()
                .text("Nice")
                .authorName("Sima")
                .item(item)
                .created(LocalDateTime.now())
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.postComment(commentDto, item.getId(), user1.getId()));

        assertEquals("400 BAD_REQUEST", exception.getMessage());
    }

    @Test
    void postCommentUserNotFound() {
        item.setOwner(user);
        itemRepository.save(item);
        User user1 = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        userRepository.save(user1);
        Booking booking = Booking.builder()
                .booker(user1)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);
        CommentDto commentDto = CommentDto.builder()
                .text("Nice")
                .authorName("Sima")
                .item(item)
                .created(LocalDateTime.now())
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.postComment(commentDto, item.getId(), 99));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void postCommentItemNotFound() {
        item.setOwner(user);
        itemRepository.save(item);
        User user1 = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        userRepository.save(user1);
        Booking booking = Booking.builder()
                .booker(user1)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);
        CommentDto commentDto = CommentDto.builder()
                .text("Nice")
                .authorName("Sima")
                .item(item)
                .created(LocalDateTime.now())
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.postComment(commentDto, 99, user1.getId()));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    @Transactional
    void update() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item1 = Item.builder()
                .name("DrillTop")
                .description("Big Drill")
                .available(true)
                .owner(user)
                .build();
        Item item2 = itemService.update(1, 1, item1);
        assertNotNull(item2);
        assertEquals(item2.getName(), item1.getName());
        assertEquals(item2.getDescription(), item1.getDescription());
        assertEquals(item2.getOwner().getId(), item1.getOwner().getId());
    }

    @Test
    @Transactional
    void updateUserNotFound() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item1 = Item.builder()
                .name("DrillTop")
                .description("Big Drill")
                .available(true)
                .owner(user)
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.update(99, 1, item1));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    @Transactional
    void updateItemNotFound() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item1 = Item.builder()
                .name("DrillTop")
                .description("Big Drill")
                .available(true)
                .owner(user)
                .build();
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> itemService.update(1, 99, item1));

        assertEquals("Unable to find ru.practicum.shareit.item.model.Item with id 99", exception.getMessage());
    }

    @Test
    void get() {
        item.setOwner(user);
        itemRepository.save(item);
        ItemDtoCommentResponse itemDtoCommentResponse = itemService.get(1, 1);

        assertEquals(1, itemDtoCommentResponse.getOwner().getId());

    }

    @Test
    void getUserNotFound() {
        item.setOwner(user);
        itemRepository.save(item);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.get(1, 99));

        assertEquals("404 NOT_FOUND", exception.getMessage());

    }

    @Test
    void getItemNotFound() {
        item.setOwner(user);
        itemRepository.save(item);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.get(99, 1));

        assertEquals("404 NOT_FOUND", exception.getMessage());

    }

    @Test
    void getItemOwner() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item2 = Item.builder()
                .owner(user)
                .name("Drill2")
                .description("Mini Drill2")
                .available(true)
                .build();
        itemRepository.save(item2);

        List<ItemDtoCommentResponse> itemDtoCommentResponseList = itemService.getItemOwner(user.getId());
        assertEquals(itemDtoCommentResponseList.size(), 2);
        assertEquals(itemDtoCommentResponseList.get(0).getOwner().getId(), user.getId());
        assertEquals(itemDtoCommentResponseList.get(1).getOwner().getId(), user.getId());
    }

    @Test
    void getItemOwnerUserNotFound() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item2 = Item.builder()
                .owner(user)
                .name("Drill2")
                .description("Mini Drill2")
                .available(true)
                .build();
        itemRepository.save(item2);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> itemService.getItemOwner(99));

        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void searchItem() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item2 = Item.builder()
                .owner(user)
                .name("Drill2")
                .description("Mini Drill2")
                .available(true)
                .build();
        itemRepository.save(item2);

        List<Item> itemList = itemService.searchItem("Drill");
        assertEquals(itemList.size(), 2);
    }

    @Test
    void searchItemEmptyText() {
        item.setOwner(user);
        itemRepository.save(item);
        Item item2 = Item.builder()
                .owner(user)
                .name("Drill2")
                .description("Mini Drill2")
                .available(true)
                .build();
        itemRepository.save(item2);

        List<Item> itemList = itemService.searchItem("");
        assertEquals(itemList.size(), 0);
    }
}