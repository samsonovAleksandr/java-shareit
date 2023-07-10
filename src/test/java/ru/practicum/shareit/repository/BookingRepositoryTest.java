package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.time.LocalDateTime;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private ItemEntityRepository itemRepository;

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private User user1;

    private Item item;

    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Sima")
                .email("sima@sima.ru")
                .build();
        user1 = User.builder()
                .name("Sima1")
                .email("sima@sima.ru1")
                .build();
        item = Item.builder()
                .name("Drill")
                .available(true)
                .owner(user1)
                .description("Mini Drill")
                .build();
        booking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    @Transactional
    void findBookingOwner() {
    }

    @Test
    void findAllByItemIdAndOwnerId() {
    }

    @Test
    void findBookingOwnerOrBooker() {
    }

    @Test
    void findAllByOwnerIdOrderByStartDesc() {
    }

    @Test
    void findAllByOwnerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByOwnerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByOwnerIdAndItemIn() {
    }

    @Test
    void findAllByOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }
}