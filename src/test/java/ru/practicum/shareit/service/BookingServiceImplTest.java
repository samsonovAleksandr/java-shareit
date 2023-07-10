package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.StateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {
    private final BookingService bookingService;

    private final UserEntityRepository userRepository;

    private final ItemEntityRepository itemRepository;

    private final BookingRepository bookingRepository;

    private User user;

    private Item item;

    private User user1;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        item = Item.builder()
                .name("Drill")
                .description("Mini Drill")
                .owner(user)
                .available(true)
                .build();
        user1 = User.builder()
                .name("Dasha")
                .email("dasha@mail.com")
                .build();

    }

    @Test
    void create() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto bookingDto = bookingService.create(bookingRequestDto, 2);

        assertEquals(bookingDto.getBooker().getId(), 2);
    }

    @Test
    void createUserNotFound() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.create(bookingRequestDto, 99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    }

    @Test
    void createItemNotFound() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(99);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.create(bookingRequestDto, 1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    }

    @Test
    void createTimeError() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.create(bookingRequestDto, 99));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

    }

    @Test
    void createItemFalseAvailable() {
        userRepository.save(user);
        userRepository.save(user1);
        item.setAvailable(false);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.create(bookingRequestDto, 2));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void createBookingOwnerUser() {
        userRepository.save(user);
        userRepository.save(user1);
        item.setAvailable(false);
        itemRepository.save(item);
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1);
        bookingRequestDto.setId(1);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.create(bookingRequestDto, 1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void update() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking);
        BookingDto bookingDto = bookingService.update(1, 1, true);

        assertEquals(bookingDto.getItem().getName(), item.getName());
    }

    @Test
    @Transactional
    void updateBookingNotFound() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.update(1, 99, true));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void updateApprovedFail() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.update(1, 1, true));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void get() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        BookingDto bookingDto = bookingService.get(1, 1);
        assertEquals(bookingDto.getItem().getName(), item.getName());
    }

    @Test
    void getBookingNotFound() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.get(1, 99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getBookingNotFoundList() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bookingService.getBookingByUserId(State.ALL, 1, "user", 1, 5));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getBookingByUserId() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<BookingDto> list = bookingService.getBookingByUserId(State.ALL, 2, "user", 1, 5);

        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getItem().getName(), item.getName());
    }

    @Test
    void getBookingByUserIdUnsupportedStatus() {
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(item);
        Booking booking = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        StateException exception = assertThrows(
                StateException.class,
                () -> bookingService.getBookingByUserId(State.UNSUPPORTED_STATUS, 1, "user", 1, 5));

        assertEquals("UNSUPPORTED_STATUS", exception.getMessage());

    }
}